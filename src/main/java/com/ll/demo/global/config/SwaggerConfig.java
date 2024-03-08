package com.ll.demo.global.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        // SecurityRequirement 객체 생성 및 JWT를 인증 범위에 추가
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        // 보안 스킴 초기화 및 설정
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                        .name(jwt)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );

        // OpenAPI 객체 생성 및 정보 설정
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }


    // API 기본 정보 제공 메소드
    private Info apiInfo() {
        return new Info()
                .title("API Test")
                .description("Swagger UI for Img_Forest")
                .version("1.0.0");
    }
}
