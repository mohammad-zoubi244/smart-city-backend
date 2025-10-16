package com.mzoubi.smartcity.modules.weather.service;

import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;

import java.util.List;

public interface WeatherService {
    List<WeatherDto> getAllWeather();
    WeatherDto getWeatherById(Long id);
    List<WeatherDto> getWeatherByCityId(Long cityId);
    WeatherDto fetchAndSaveWeatherForCity(Long cityId);
    void deleteWeather(Long id);
}
