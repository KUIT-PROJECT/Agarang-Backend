package com.kuit.agarang.global.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI springOpenApi() {
    SecurityScheme apiKey = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .name("Authorization")
            .scheme("bearer")
            .bearerFormat("JWT");

    SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("Bearer Token");

    return new OpenAPI()
            .info(new Info()
                    .title("Agarang's API Docs")
                    .description("생성형 AI기반의 가족이 함께하는 태교 서비스")
                    .version("v0.0.1"))
            .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
            .addSecurityItem(securityRequirement);
  }
}
