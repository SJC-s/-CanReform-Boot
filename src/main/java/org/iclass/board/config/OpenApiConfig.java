package org.iclass.board.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    // http://localhost:8080/swagger-ui/swagger-ui/index.html : swagger UI
    // http://localhost:8080/v3/api-docs : Json Type
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("jwt.secret",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("jwt.secret"))
                .info(new Info()
                        .title("CanReForm API 명세서")
                        .description("CanReForm 프로젝트의 API 문서입니다.")
                        .version("1.0.0"));
    }
}
