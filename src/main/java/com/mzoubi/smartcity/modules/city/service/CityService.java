package com.mzoubi.smartcity.modules.city.service;

import com.mzoubi.smartcity.modules.city.dto.CityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface CityService {

    Page<CityDto> getAllCities(PageRequest pageRequest);
    CityDto getCityById(Long id);
    CityDto addCity(CityDto cityDto);
    CityDto updateCity(Long id, CityDto cityDto);
    void deleteCity(Long id);
}
