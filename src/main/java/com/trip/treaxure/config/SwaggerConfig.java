package com.trip.treaxure.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "TreaXure API 문서", description = "TreaXure 프로젝트의 API 명세입니다.", version = "v1"))
public class SwaggerConfig {

    // Swagger에서 JWT 인증 헤더 설정
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }

    // 통합된 모든 API 보기
    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("All APIs")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Auth API")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
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
    public GroupedOpenApi favoriteApi() {
        return GroupedOpenApi.builder()
                .group("Favorite API")
                .pathsToMatch("/api/favorites/**")
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
