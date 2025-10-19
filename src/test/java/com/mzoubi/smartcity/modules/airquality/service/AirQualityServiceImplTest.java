package com.mzoubi.smartcity.modules.airquality.service;

import com.mzoubi.smartcity.common.exceptions.ResourceNotFoundException;
import com.mzoubi.smartcity.config.OpenWeatherClient;
import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;
import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import com.mzoubi.smartcity.modules.airquality.mapper.AirQualityMapper;
import com.mzoubi.smartcity.modules.airquality.repository.AirQualityRepository;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.city.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AirQualityServiceImplTest {

    @Mock
    private AirQualityRepository airQualityRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private AirQualityMapper airQualityMapper;
    @Mock
    private OpenWeatherClient openWeatherClient;
    @InjectMocks
    private AirQualityServiceImpl airQualityService;

    private City city;
    private AirQuality airQuality;
    private AirQualityDto airQualityDto;

    @BeforeEach
    void setUp() {
        city = City.builder()
                .id(1L)
                .name("Amman")
                .latitude(31.95)
                .longitude(35.91)
                .build();

        airQuality = AirQuality.builder()
                .id(1L)
                .city(city)
                .aqi(2)
                .pm25(15.26)
                .pm10(47.98)
                .no2(47.98)
                .o3(47.98)
                .co(104.49)
                .so2(1.66)
                .timestamp(LocalDateTime.now())
                .build();

        airQualityDto = new AirQualityDto(
                null,
                "Amman"
                , 2
                , 15.26
                , 47.98
                , 47.98
                , 47.98
                , 104.49
                , 1.66
                , LocalDateTime.now());
    }

    @Test
    @DisplayName("getAllAirQuality_WhenRecordsExist_ShouldReturnListOfAirQuality")
    void getAllAirQuality_WhenRecordsExist_ShouldReturnListOfAirQuality() {
        when(airQualityRepository.findAll()).thenReturn(List.of(airQuality));
        when(airQualityMapper.toDto(airQuality)).thenReturn(airQualityDto);

        List<AirQualityDto> result = airQualityService.getAllAirQuality();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().cityName()).isEqualTo("Amman");
        verify(airQualityRepository).findAll();
    }

    @Test
    @DisplayName("getAllAirQuality_WhenNoRecords_ShouldReturnEmptyList")
    void getAllAirQuality_WhenNoRecords_ShouldReturnEmptyList() {
        when(airQualityRepository.findAll()).thenReturn(List.of());

        List<AirQualityDto> result = airQualityService.getAllAirQuality();

        assertThat(result).isEmpty();
        verify(airQualityRepository).findAll();
    }

    @Test
    @DisplayName("getAirQualityById_WhenWeatherExists_ShouldReturnAirQuality")
    void getAirQualityById_WhenWeatherExists_ShouldReturnAirQuality() {
        when(airQualityRepository.findById(1L)).thenReturn(Optional.of(airQuality));
        when(airQualityMapper.toDto(airQuality)).thenReturn(airQualityDto);

        AirQualityDto result = airQualityService.getAirQualityById(1L);

        assertThat(result).isNotNull();
        assertThat(result.cityName()).isEqualTo("Amman");
        verify(airQualityRepository).findById(1L);
    }

    @Test
    @DisplayName("getAirQualityById_WhenAirQualityNotFound_ShouldThrowException")
    void getAirQualityById_WhenAirQualityNotFound_ShouldThrowException() {
        when(airQualityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> airQualityService.getAirQualityById(1L));

        verify(airQualityRepository).findById(1L);
        verifyNoMoreInteractions(airQualityMapper);
    }

    @Test
    @DisplayName("getAirQualityByCityId_WhenWeatherExists_ShouldReturnAirQualityList")
    void getAirQualityByCityId_WhenWeatherExists_ShouldReturnAirQualityList() {
        when(airQualityRepository.findAirQualityByCityId(1L)).thenReturn(List.of(airQuality));
        when(airQualityMapper.toDto(airQuality)).thenReturn(airQualityDto);

        List<AirQualityDto> result = airQualityService.getAirQualityByCityId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().cityName()).isEqualTo("Amman");
        verify(airQualityRepository).findAirQualityByCityId(1L);
    }

    @Test
    @DisplayName("getAirQualityByCityId_WhenNoAirQualityFound_ShouldReturnEmptyList")
    void getAirQualityByCityId_WhenNoAirQualityFound_ShouldReturnEmptyList() {
        when(airQualityRepository.findAirQualityByCityId(1L)).thenReturn(List.of());

        List<AirQualityDto> result = airQualityService.getAirQualityByCityId(1L);

        assertThat(result).isEmpty();
        verify(airQualityRepository).findAirQualityByCityId(1L);
    }

    @Test
    @DisplayName("fetchAndSaveAirQualityForCity_WhenCityExists_ShouldFetchMapAndSaveAirQuality")
    void fetchAndSaveAirQualityForCity_WhenCityExists_ShouldFetchMapAndSaveAirQuality() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(openWeatherClient.fetchAirQualityForACity(city)).thenReturn(airQualityDto);
        when(airQualityMapper.toNewEntity(airQualityDto)).thenReturn(airQuality);
        when(airQualityRepository.save(any(AirQuality.class))).thenReturn(airQuality);
        when(airQualityMapper.toDto(airQuality)).thenReturn(airQualityDto);

        AirQualityDto result = airQualityService.fetchAndSaveAirQualityForCity(1L);

        assertThat(result).isNotNull();
        assertThat(result.cityName()).isEqualTo("Amman");
        verify(cityRepository).findById(1L);
        verify(openWeatherClient).fetchAirQualityForACity(city);
        verify(airQualityRepository).save(any(AirQuality.class));
    }

    @Test
    @DisplayName("fetchAndSaveAirQualityForCity_WhenCityNotFound_ShouldThrowException")
    void fetchAndSaveAirQualityForCity_WhenCityNotFound_ShouldThrowException() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> airQualityService.fetchAndSaveAirQualityForCity(99L));

        verify(cityRepository).findById(99L);
        verifyNoInteractions(openWeatherClient, airQualityRepository, airQualityMapper);
    }

    @Test
    @DisplayName("deleteAirQuality_WhenAirQualityExists_ShouldDeleteSuccessfully")
    void deleteAirQuality_WhenAirQualityExists_ShouldDeleteSuccessfully() {
        when(airQualityRepository.existsById(1L)).thenReturn(true);

        airQualityService.deleteAirQuality(1L);

        verify(airQualityRepository).existsById(1L);
        verify(airQualityRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteAirQuality_WhenAirQualityNotFound_ShouldThrowException")
    void deleteAirQuality_WhenAirQualityNotFound_ShouldThrowException() {
        when(airQualityRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> airQualityService.deleteAirQuality(1L));

        verify(airQualityRepository).existsById(1L);
        verify(airQualityRepository, never()).deleteById(anyLong());
    }
}
