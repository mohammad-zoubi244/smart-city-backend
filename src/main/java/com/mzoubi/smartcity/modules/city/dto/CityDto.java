package com.mzoubi.smartcity.modules.city.dto;

public record CityDto(
        Long id,
        String name,
        String country,
        Double latitude,
        Double longitude,
        String timezone) {}
