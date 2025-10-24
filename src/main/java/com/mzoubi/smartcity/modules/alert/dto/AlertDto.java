package com.mzoubi.smartcity.modules.alert.dto;

public record AlertDto(
        String cityName,
        String type,
        String message,
        double value,
        double threshold) {
}
