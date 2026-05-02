package com.mailchimp.marketing.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mailchimpOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mailchimp Marketing API")
                        .description("Spring Boot backend for seamless Mailchimp email marketing operations. "
                                + "Manage audiences, subscribers, campaigns, and templates via RESTful endpoints.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Mailchimp Marketing")
                                .email("support@example.com"))
                        .license(new License()
                                .name("MIT License")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }
}
