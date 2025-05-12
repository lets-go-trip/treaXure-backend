package com.trip.treaxure.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "TreaXure API 문서", description = "TreaXure 프로젝트의 API 명세입니다.", version = "v1"))
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User API")
                .pathsToMatch("/api/users/**")
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
    public GroupedOpenApi voteApi() {
        return GroupedOpenApi.builder()
                .group("Vote API")
                .pathsToMatch("/api/votes/**")
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
    public GroupedOpenApi likeApi() {
        return GroupedOpenApi.builder()
                .group("Like API")
                .pathsToMatch("/api/likes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi missionApi() {
        return GroupedOpenApi.builder()
                .group("Mission API")
                .pathsToMatch("/api/missions/**")
                .build();
    }
}
