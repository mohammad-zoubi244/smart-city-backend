package com.mzoubi.smartcity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI smartCityOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart City REST API")
                        .description("API documentation for Smart City platform integrating weather, air quality, and transport data.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Mohammad Zoubi")
                                .email("mohammad.zoubi244@gmail.com")
                                .url("https://github.com/mohammad-zoubi244")));
    }
}
