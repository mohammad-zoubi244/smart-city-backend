package com.mzoubi.smartcity.modules.city.service;

import com.mzoubi.smartcity.modules.city.dto.CityDto;

import java.util.List;
import java.util.Optional;

public interface CityService {

    List<CityDto> getAllCities();
    CityDto getCityById(Long id);
    CityDto addCity(CityDto cityDto);
    CityDto updateCity(Long id, CityDto cityDto);
    void deleteCity(Long id);
}
