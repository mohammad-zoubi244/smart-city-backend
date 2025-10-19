package com.mzoubi.smartcity.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.mzoubi.smartcity.config.properties.OpenWeatherProperties;
import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;
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

    public WeatherDto fetchWeatherForACity(City city) {
        JsonNode rawData = fetchWeatherData(city);
        return mapToWeatherDto(rawData, city);
    }

    public AirQualityDto fetchAirQualityForACity(City city) {
        JsonNode rawData = fetchAirQualityData(city);
        return mapToAirQualityDto(rawData, city);
    }

    private JsonNode fetchWeatherData(City city) {
        log.debug("Fetching weather data for city {}", city.getName());
        try {
            return webClient.get()
                    .uri(returnWeatherDataUrl(
                            city.getLatitude(),
                            city.getLongitude()))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to fetch weather data for city {}: {}", city.getName(), e.getMessage());
            throw new RuntimeException("Failed to fetch weather data", e);
        }
    }

    private JsonNode fetchAirQualityData(City city) {
        log.debug("Fetching air quality data for city {}", city.getName());
        try {
            return webClient.get()
                    .uri(returnAirQualityDataUrl(
                            city.getLatitude(),
                            city.getLongitude()))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to fetch air quality data for city {}: {}", city.getName(), e.getMessage());
            throw new RuntimeException("Failed to fetch air quality data", e);
        }
    }

    private WeatherDto mapToWeatherDto(JsonNode jsonNode, City city) {
        if (jsonNode == null || !jsonNode.has("main") || jsonNode.get("main").isEmpty()) {
            log.warn("No weather data available for city {}", city.getName());
            return null;
        }

        double temp = jsonNode.path("main").path("temp").asDouble();
        double humidity = jsonNode.path("main").path("humidity").asDouble();
        double windSpeed = jsonNode.path("wind").path("speed").asDouble();
        String condition = jsonNode.path("weather").get(0).path("main").asText();

        return new WeatherDto(
                null,
                city.getName(),
                temp,
                humidity,
                windSpeed,
                condition,
                LocalDateTime.now()
        );

    }

    private AirQualityDto mapToAirQualityDto(JsonNode jsonNode, City city) {
        if (jsonNode == null || !jsonNode.has("list") || jsonNode.get("list").isEmpty()) {
            log.warn("No air quality data available for city {}", city.getName());
            return null;
        }

        JsonNode data = jsonNode.get("list").get(0);
        JsonNode components = data.get("components");

        return new AirQualityDto(
                null,
                city.getName(),
                data.get("main").get("aqi").asInt(),
                components.get("pm2_5").asDouble(),
                components.get("pm10").asDouble(),
                components.get("no2").asDouble(),
                components.get("o3").asDouble(),
                components.get("co").asDouble(),
                components.get("so2").asDouble(),
                LocalDateTime.now());
    }

    private String returnWeatherDataUrl(Double lat, Double lon) {
        return String.format("%s%s?lat=%f&lon=%f&appid=%s&units=metric",
                openWeatherProperties.baseUrl(),
                openWeatherProperties.weatherPath(),
                lat,
                lon,
                openWeatherProperties.apikey());
    }

    private String returnAirQualityDataUrl(Double lat, Double lon) {
        return String.format("%s%s?lat=%f&lon=%f&appid=%s&units=metric",
                openWeatherProperties.baseUrl(),
                openWeatherProperties.airPollutionPath(),
                lat,
                lon,
                openWeatherProperties.apikey());
    }
}
