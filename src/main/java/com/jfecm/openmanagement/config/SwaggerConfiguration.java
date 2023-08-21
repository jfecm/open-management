package com.jfecm.openmanagement.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(createApiInfo());
    }

    private Info createApiInfo() {
        return new Info()
                .title("OPEN-MANAGEMENT REST API")
                .description("Open-management is an application for simple product management.")
                .version("1.0")
                .contact(createContactInfo())
                .license(createLicenseInfo());
    }

    private Contact createContactInfo() {
        return new Contact()
                .name("JFECM")
                .url("https://github.com/joaquincorimayo")
                .email("jfecm@gmail.com");
    }

    private License createLicenseInfo() {
        return new License()
                .name("License of API")
                .url("https://github.com/joaquincorimayo/open-management/blob/master/LICENSE");
    }

}