package com.mzoubi.smartcity.modules.airquality.service;

import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;

import java.util.List;

public interface AirQualityService {

    List<AirQualityDto> getAllAirQuality();
    AirQualityDto getAirQualityById(Long id);
    List<AirQualityDto> getAirQualityByCityId(Long cityId);
    AirQualityDto fetchAndSaveAirQualityForCity(Long cityId);
    void deleteAirQuality(Long id);
}
