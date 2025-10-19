package com.mzoubi.smartcity.modules.weather.service;

import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.city.repository.CityRepository;
import com.mzoubi.smartcity.config.OpenWeatherClient;
import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;
import com.mzoubi.smartcity.modules.weather.entity.Weather;
import com.mzoubi.smartcity.modules.weather.mapper.WeatherMapper;
import com.mzoubi.smartcity.modules.weather.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mzoubi.smartcity.common.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @Mock
    private WeatherMapper weatherMapper;
    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private OpenWeatherClient openWeatherClient;
    @InjectMocks
    private WeatherServiceImpl weatherService;

    private City city;
    private WeatherDto weatherDto;
    private Weather weather;

    @BeforeEach
    void setUp() {
        city = City.builder()
                .id(1L)
                .name("Amman")
                .latitude(31.95)
                .longitude(35.91)
                .build();

        weatherDto = new WeatherDto(
                null,
                "Amman",
                25.0,
                60.0,
                5.0,
                "Clear",
                LocalDateTime.now()
        );

        weather = Weather.builder()
                .id(1L)
                .city(city)
                .temperature(25.0)
                .humidity(60.0)
                .windSpeed(5.0)
                .condition("Clear")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("getAllWeather_WhenRecordsExist_ShouldReturnListOfWeather")
    void getAllWeather_WhenRecordsExist_ShouldReturnListOfWeather() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("id"));
        when(weatherRepository.findAll()).thenReturn(List.of(weather));
        when(weatherMapper.toDto(weather)).thenReturn(weatherDto);

        Page<WeatherDto> result = weatherService.getAllWeather(pageRequest);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().getFirst().cityName()).isEqualTo("Amman");
        verify(weatherRepository).findAll();
    }

    @Test
    @DisplayName("getAllWeather_WhenNoRecords_ShouldReturnEmptyList")
    void getAllWeather_WhenNoRecords_ShouldReturnEmptyList() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("id"));
        when(weatherRepository.findAll()).thenReturn(List.of());

        Page<WeatherDto> result = weatherService.getAllWeather(pageRequest);

        assertThat(result).isEmpty();
        verify(weatherRepository).findAll();
    }

    @Test
    @DisplayName("getWeatherById_WhenWeatherExists_ShouldReturnWeather")
    void getWeatherById_WhenWeatherExists_ShouldReturnWeather() {
        when(weatherRepository.findById(1L)).thenReturn(Optional.of(weather));
        when(weatherMapper.toDto(weather)).thenReturn(weatherDto);

        WeatherDto result = weatherService.getWeatherById(1L);

        assertThat(result).isNotNull();
        assertThat(result.cityName()).isEqualTo("Amman");
        verify(weatherRepository).findById(1L);
    }

    @Test
    @DisplayName("getWeatherById_WhenWeatherNotFound_ShouldThrowException")
    void getWeatherById_WhenWeatherNotFound_ShouldThrowException() {
        when(weatherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> weatherService.getWeatherById(1L));

        verify(weatherRepository).findById(1L);
        verifyNoMoreInteractions(weatherMapper);
    }


    @Test
    @DisplayName("getWeatherByCityId_WhenWeatherExists_ShouldReturnWeatherList")
    void getWeatherByCityId_WhenWeatherExists_ShouldReturnWeatherList() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("id"));
        Page<Weather> weatherPage = new PageImpl<>(List.of(weather), pageRequest, 1);

        when(weatherRepository.findByCityId(1L,pageRequest)).thenReturn(weatherPage);
        when(weatherMapper.toDto(weather)).thenReturn(weatherDto);

        Page<WeatherDto> result = weatherService.getWeatherByCityId(1L,pageRequest);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().getFirst().cityName()).isEqualTo("Amman");
        verify(weatherRepository).findByCityId(1L,pageRequest);
    }

    @Test
    @DisplayName("getWeatherByCityId_WhenNoWeatherFound_ShouldReturnEmptyList")
    void getWeatherByCityId_WhenNoWeatherFound_ShouldReturnEmptyList() {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("id"));
        Page<Weather> weatherPage = new PageImpl<>(List.of(), pageRequest, 1);

        when(weatherRepository.findByCityId(1L,pageRequest)).thenReturn(weatherPage);

        Page<WeatherDto> result = weatherService.getWeatherByCityId(1L,pageRequest);

        assertThat(result).isEmpty();
        verify(weatherRepository).findByCityId(1L,pageRequest);
    }


    @Test
    @DisplayName("fetchAndSaveWeatherForCity_WhenCityExists_ShouldFetchMapAndSaveWeather")
    void fetchAndSaveWeatherForCity_WhenCityExists_ShouldFetchMapAndSaveWeather() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(openWeatherClient.fetchWeatherForACity(city)).thenReturn(weatherDto);
        when(weatherMapper.toNewEntity(weatherDto)).thenReturn(weather);
        when(weatherRepository.save(any(Weather.class))).thenReturn(weather);
        when(weatherMapper.toDto(weather)).thenReturn(weatherDto);

        WeatherDto result = weatherService.fetchAndSaveWeatherForCity(1L);

        assertThat(result).isNotNull();
        assertThat(result.cityName()).isEqualTo("Amman");
        verify(cityRepository).findById(1L);
        verify(openWeatherClient).fetchWeatherForACity(city);
        verify(weatherRepository).save(any(Weather.class));
    }

    @Test
    @DisplayName("fetchAndSaveWeatherForCity_WhenCityNotFound_ShouldThrowException")
    void fetchAndSaveWeatherForCity_WhenCityNotFound_ShouldThrowException() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> weatherService.fetchAndSaveWeatherForCity(99L));

        verify(cityRepository).findById(99L);
        verifyNoInteractions(openWeatherClient, weatherRepository, weatherMapper);
    }

    @Test
    @DisplayName("deleteWeather_WhenWeatherExists_ShouldDeleteSuccessfully")
    void deleteWeather_WhenWeatherExists_ShouldDeleteSuccessfully() {
        when(weatherRepository.existsById(1L)).thenReturn(true);

        weatherService.deleteWeather(1L);

        verify(weatherRepository).existsById(1L);
        verify(weatherRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteWeather_WhenWeatherNotFound_ShouldThrowException")
    void deleteWeather_WhenWeatherNotFound_ShouldThrowException() {
        when(weatherRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> weatherService.deleteWeather(1L));

        verify(weatherRepository).existsById(1L);
        verify(weatherRepository, never()).deleteById(anyLong());
    }
}
