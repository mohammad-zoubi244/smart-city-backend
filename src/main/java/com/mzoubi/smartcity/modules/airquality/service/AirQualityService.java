package com.mzoubi.smartcity.modules.airquality.service;

import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface AirQualityService {

    Page<AirQualityDto> getAllAirQuality(PageRequest pageRequest);
    AirQualityDto getAirQualityById(Long id);
    Page<AirQualityDto> getAirQualityByCityId(Long cityId, PageRequest pageRequest);
    AirQualityDto fetchAndSaveAirQualityForCity(Long cityId);
    void deleteAirQuality(Long id);
}
