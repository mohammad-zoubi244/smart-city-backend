package com.mzoubi.smartcity.modules.airquality.service;

import com.mzoubi.smartcity.common.exceptions.ResourceNotFoundException;
import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;
import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import com.mzoubi.smartcity.modules.airquality.mapper.AirQualityMapper;
import com.mzoubi.smartcity.modules.airquality.repository.AirQualityRepository;
import com.mzoubi.smartcity.modules.alert.service.AlertService;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.city.repository.CityRepository;
import com.mzoubi.smartcity.config.OpenWeatherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirQualityServiceImpl implements AirQualityService {

    private final AirQualityRepository airQualityRepository;
    private final CityRepository cityRepository;
    private final AirQualityMapper airQualityMapper;
    private final OpenWeatherClient openWeatherClient;
    private final AlertService alertService;

    @Override
    public Page<AirQualityDto> getAllAirQuality(PageRequest pageRequest) {
        log.debug("Get all air quality.");
        return airQualityRepository.findAll(pageRequest).map(airQualityMapper::toDto);
    }

    @Override
    public AirQualityDto getAirQualityById(Long id) {
        log.debug("Get air quality with id : {}", id);
        AirQuality airQuality = airQualityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Air quality not found with id: " + id));

        return airQualityMapper.toDto(airQuality);
    }

    @Override
    public Page<AirQualityDto> getAirQualityByCityId(Long cityId,PageRequest pageRequest) {
        log.debug("Get air quality for city: {}", cityId);
        return airQualityRepository.findAirQualityByCityId(cityId,pageRequest).map(airQualityMapper::toDto);
    }

    @Override
    @Transactional
    public AirQualityDto fetchAndSaveAirQualityForCity(Long cityId) {
        log.debug("Fetching and saving air quality for city with id: {}", cityId);

        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id : " + cityId));

        AirQualityDto fetched = openWeatherClient.fetchAirQualityForACity(city);
        AirQuality airQuality = airQualityMapper.toNewEntity(fetched);
        airQuality.setCity(city);

        AirQuality saved = airQualityRepository.save(airQuality);

        alertService.evaluateAndCreatedAlerts(city, null, saved);

        return airQualityMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteAirQuality(Long id) {
        log.debug("Deleting air quality record with id: {}", id);

        if (!airQualityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Air quality not found with id: " + id);
        }

        airQualityRepository.deleteById(id);
    }
}
