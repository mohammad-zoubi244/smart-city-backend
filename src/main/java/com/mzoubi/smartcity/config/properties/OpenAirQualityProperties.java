package com.mzoubi.smartcity.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.airquality")
public record OpenAirQualityProperties(
        String baseUrl,
        String locationsPath,
        String latestPath,
        String apikey,
        Integer locationsLimit) {

}
