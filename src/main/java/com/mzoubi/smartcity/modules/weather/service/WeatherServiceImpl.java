package com.mzoubi.smartcity.modules.weather.service;

import com.mzoubi.smartcity.common.exceptions.ResourceNotFoundException;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.city.repository.CityRepository;
import com.mzoubi.smartcity.config.OpenWeatherClient;
import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;
import com.mzoubi.smartcity.modules.weather.entity.Weather;
import com.mzoubi.smartcity.modules.weather.mapper.WeatherMapper;
import com.mzoubi.smartcity.modules.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherMapper weatherMapper;
    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;
    private final OpenWeatherClient openWeatherClient;

    @Override
    public Page<WeatherDto> getAllWeather(PageRequest pageRequest) {
        log.debug("Get all weather");

        return weatherRepository.findAll(pageRequest).map(weatherMapper::toDto);
    }

    @Override
    public WeatherDto getWeatherById(Long id) {
        log.debug("Get weather by id");

        Weather weather = weatherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weather not found with id: " + id));

        return weatherMapper.toDto(weather);
    }

    @Override
    public Page<WeatherDto> getWeatherByCityId(Long cityId, PageRequest pageRequest) {
        log.debug("Get weather by city id");

        return weatherRepository.findByCityId(cityId, pageRequest)
                .map(weatherMapper::toDto);
    }

    @Override
    @Transactional
    public WeatherDto fetchAndSaveWeatherForCity(Long cityId) {
        log.debug("Fetch and save weather for a city");

        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));

        WeatherDto weatherDto = openWeatherClient.fetchWeatherForACity(city);
        Weather weather = weatherMapper.toNewEntity(weatherDto);
        weather.setCity(city);
        Weather saved = weatherRepository.save(weather);

        return weatherMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteWeather(Long id) {
        log.debug("delete a weather");

        if (!weatherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Weather not found with id: " + id);
        }
        weatherRepository.deleteById(id);
    }

}
