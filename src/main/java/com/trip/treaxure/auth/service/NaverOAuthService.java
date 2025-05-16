package com.trip.treaxure.auth.service;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.treaxure.config.NaverOAuthProperties;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverOAuthService {

    private final NaverOAuthProperties properties;
    private final MemberRepository memberRepository;

    public String getAccessToken(String code, String state) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", properties.getClientId());
        params.add("client_secret", properties.getClientSecret());
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(properties.getTokenUri(), request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody()).get("access_token").asText();
    }

    public Member getUserInfo(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                properties.getUserInfoUri(),
                HttpMethod.GET,
                request,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(response.getBody()).get("response");

        String email = responseJson.get("email").asText();
        String name = responseJson.get("name").asText();
        if (name == null || name.isEmpty()) {
            name = "사냥꾼";
        }

        Optional<Member> existing = memberRepository.findByEmail(email);
        String resolvedName = name;
        return existing.orElseGet(() -> {
            Member member = Member.builder()
                    .email(email)
                    .nickname(resolvedName)
                    .password("naver_user")
                    .isActive(true)
                    .role(Member.MemberRole.USER)
                    .build();
            return memberRepository.save(member);
        });
    }
}
