package com.mzoubi.smartcity.modules.city.service;

import com.mzoubi.smartcity.common.exceptions.ResourceNotFoundException;
import com.mzoubi.smartcity.modules.city.dto.CityDto;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.city.mapper.CityMapper;
import com.mzoubi.smartcity.modules.city.repository.CityRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityServiceImpl cityService;

    private City city;
    private CityDto cityDto;

    @BeforeEach
    void setUp() {
        city = new City();
        city.setId(1L);
        city.setName("Amman");
        city.setCountry("Jordan");
        city.setLatitude(31.9539);
        city.setLongitude(35.9106);
        city.setTimezone("Asia/Amman");

        cityDto = new CityDto(1L, "Amman", "Jordan", 31.9539, 35.9106, "Asia/Amman");
    }

    @Test
    @DisplayName("getAllCities_ShouldReturnListOfCity_WhenCitiesExist")
    void getAllCities_ShouldReturnListOfCity_WhenCitiesExist() {
        when(cityRepository.findAll()).thenReturn(List.of(city));
        when(cityMapper.toDto(city)).thenReturn(cityDto);

        List<CityDto> result = cityService.getAllCities();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Amman");
        verify(cityRepository, times(1)).findAll();
        verify(cityMapper, times(1)).toDto(city);
    }

    @Test
    @DisplayName("getCityById_ShouldReturnCity_WhenCityExist")
    void getCityById_ShouldReturnCity_WhenCityExist() {
        when(cityRepository.findById(1L)).thenReturn(Optional.ofNullable(city));
        when(cityMapper.toDto(city)).thenReturn(cityDto);

        CityDto result = cityService.getCityById(1L);

        assertThat(result.name()).isEqualTo("Amman");
        verify(cityRepository).findById(1L);
    }

    @Test
    @DisplayName("getCityById_ShouldThrowException_WhenCityNotFound")
    void getCityById_ShouldThrowException_WhenCityNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.getCityById(1L));
    }

    @Test
    @DisplayName("addCity_ShouldSaveAndReturnCityDto")
    void addCity_ShouldSaveAndReturnCityDto() {
        when(cityMapper.toEntity(cityDto)).thenReturn(city);
        when(cityRepository.save(any(City.class))).thenReturn(city);
        when(cityMapper.toDto(city)).thenReturn(cityDto);

        CityDto result = cityService.addCity(cityDto);

        assertThat(result.name()).isEqualTo("Amman");
        verify(cityRepository).save(any(City.class));
    }

    @Test
    @DisplayName("updateCity_ShouldUpdateExistingCity_WhenCityExists")
    void updateCity_ShouldUpdateExistingCity_WhenCityExists() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(cityRepository.save(any(City.class))).thenReturn(city);
        when(cityMapper.toDto(city)).thenReturn(cityDto);

        CityDto result = cityService.updateCity(1L, cityDto);

        assertThat(result.name()).isEqualTo("Amman");
        verify(cityRepository).save(any(City.class));
    }

    @Test
    @DisplayName("updateCity_ShouldThrowException_WhenCityNotFound")
    void updateCity_ShouldThrowException_WhenCityNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(1L, cityDto));
    }

    @Test
    @DisplayName("deleteCity_ShouldDeleteCity_WhenExists")
    void deleteCity_ShouldDeleteCity_WhenExists() {
        when(cityRepository.existsById(1L)).thenReturn(true);

        cityService.deleteCity(1L);

        verify(cityRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteCity_ShouldThrowException_WhenCityNotFound")
    void deleteCity_ShouldThrowException_WhenCityNotFound() {
        when(cityRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(1L));
    }
}
