package com.mzoubi.smartcity.modules.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.mzoubi.smartcity.config.OpenWeatherProperties;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherClient {

    private final WebClient webClient;
    private final OpenWeatherProperties openWeatherProperties;

    public WeatherDto fetchWeatherForCity(City city) {
        try {
            log.debug("Fetching weather from openweather for city {}", city.getName());

            JsonNode response = webClient.get()
                    .uri(returnOpenWeatherFullWeatherDataUrl(
                            city.getLatitude(),
                            city.getLongitude()))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(); // synchronous, remove for reactive

            double temp = response.path("main").path("temp").asDouble();
            double humidity = response.path("main").path("humidity").asDouble();
            double windSpeed = response.path("wind").path("speed").asDouble();
            String condition = response.path("weather").get(0).path("main").asText();

            return new WeatherDto(
                    null,
                    city.getName(),
                    temp,
                    humidity,
                    windSpeed,
                    condition,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("Failed to fetch weather for city {}: {}", city.getName(), e.getMessage());
            throw new RuntimeException("Failed to fetch weather data", e);
        }
    }

    private String returnOpenWeatherFullWeatherDataUrl(Double lat, Double lon) {
        return String.format("%s%s?lat=%f&lon=%f&appid=%s&units=metric",
                openWeatherProperties.baseUrl(),
                openWeatherProperties.weatherPath(),
                lat,
                lon,
                openWeatherProperties.apikey());
    }
}
