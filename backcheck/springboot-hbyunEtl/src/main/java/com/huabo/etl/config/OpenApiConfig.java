package com.huabo.etl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
	    return new OpenAPI()
	            .info(new Info()
	                    .title("智能审计 API")
	                    .version("1.0.0")
	                    .description("智能审计模块的REST API文档")
	                    .contact(new Contact()
	                            .name("技术支持")
	                            .email("dev@example.com")
	                            .url("https://www.wenxin.example.com"))
	                    .license(new License()
	                            .name("Apache 2.0")
	                            .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
	            .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"))
	            .components(new io.swagger.v3.oas.models.Components()
	                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
	                            .type(SecurityScheme.Type.HTTP)
	                            .scheme("bearer")
	                            .bearerFormat("JWT")));
	}
}
