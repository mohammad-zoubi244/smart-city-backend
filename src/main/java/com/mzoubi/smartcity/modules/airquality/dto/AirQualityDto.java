package com.mzoubi.smartcity.modules.airquality.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record AirQualityDto(
        Long id,
        String cityName,
        Integer aqi,
        Double pm25,
        Double pm10,
        Double no2,
        Double o3,
        Double co,
        Double so2,
        LocalDateTime timestamp) {
}
