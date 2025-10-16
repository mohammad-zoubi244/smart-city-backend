package com.mzoubi.smartcity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.openweathermap")
public record OpenWeatherProperties(
        String baseUrl,
        String weatherPath,
        String apikey
) {
}
