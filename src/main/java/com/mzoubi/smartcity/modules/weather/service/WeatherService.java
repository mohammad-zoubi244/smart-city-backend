package com.mzoubi.smartcity.modules.weather.service;

import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface WeatherService {
    Page<WeatherDto> getAllWeather(PageRequest pageRequest);
    WeatherDto getWeatherById(Long id);
    Page<WeatherDto> getWeatherByCityId(Long cityId, PageRequest pageRequest);
    WeatherDto fetchAndSaveWeatherForCity(Long cityId);
    void deleteWeather(Long id);
}
