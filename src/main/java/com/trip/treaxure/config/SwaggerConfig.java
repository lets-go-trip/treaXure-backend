package com.trip.treaxure.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "TreaXure API 문서", description="TreaXure 프로젝트의 API 명세입니다.", version="v1"))
public class SwaggerConfig {

}
