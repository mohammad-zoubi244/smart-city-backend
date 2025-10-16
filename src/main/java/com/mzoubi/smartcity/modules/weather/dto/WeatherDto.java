package com.mzoubi.smartcity.modules.weather.dto;

import java.time.LocalDateTime;

public record WeatherDto (
        Long id,
        String cityName,
        Double temperature,
        Double humidity,
        Double windSpeed,
        String condition,
        LocalDateTime timestamp){
}
