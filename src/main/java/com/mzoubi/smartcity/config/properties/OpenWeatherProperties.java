package com.mzoubi.smartcity.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.openweathermap")
public record OpenWeatherProperties(
        String baseUrl,
        String weatherPath,
        String airPollutionPath,
        String apikey
) {
}
