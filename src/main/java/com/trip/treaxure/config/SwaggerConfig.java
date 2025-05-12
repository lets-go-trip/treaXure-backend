package com.trip.treaxure.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "TreaXure API 문서",
        description = "TreaXure 프로젝트의 API 명세입니다.",
        version = "v1"
    )
)
public class SwaggerConfig {

    // 기존에 개별적으로 묶어두신 그룹들은 그대로 두셔도 되고,
    // 한번에 전체 API를 보고 싶으시면 아래 All API 그룹을 사용하세요.

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All API")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("Member API")
                .pathsToMatch("/api/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi placeApi() {
        return GroupedOpenApi.builder()
                .group("Place API")
                .pathsToMatch("/api/places/**")
                .build();
    }

    @Bean
    public GroupedOpenApi missionApi() {
        return GroupedOpenApi.builder()
                .group("Mission API")
                .pathsToMatch("/api/missions/**")
                .build();
    }

    @Bean
    public GroupedOpenApi boardApi() {
        return GroupedOpenApi.builder()
                .group("Board API")
                .pathsToMatch("/api/boards/**")
                .build();
    }

    @Bean
    public GroupedOpenApi voteApi() {
        return GroupedOpenApi.builder()
                .group("Vote API")
                .pathsToMatch("/api/votes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi likeApi() {
        return GroupedOpenApi.builder()
                .group("Favorite API")
                .pathsToMatch("/api/favorites/**")
                .build();
    }
    
    // 혹은 auth 엔드포인트도 보고 싶다면:
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Auth API")
                .pathsToMatch("/api/auth/**")
                .build();
    }

}
