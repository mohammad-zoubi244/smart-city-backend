package com.mzoubi.smartcity.modules.city.service;

import com.mzoubi.smartcity.common.exceptions.ResourceNotFoundException;
import com.mzoubi.smartcity.modules.city.dto.CityDto;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.city.mapper.CityMapper;
import com.mzoubi.smartcity.modules.city.repository.CityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityMapper cityMapper;
    private final CityRepository cityRepository;

    @Override
    public Page<CityDto> getAllCities(PageRequest pageRequest) {
        log.debug("Get all cities");

        return cityRepository.findAll(pageRequest)
                .map(cityMapper::toDto);
    }

    @Override
    public CityDto getCityById(Long id) {
        log.debug("Get city with id: {}",id);

       City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        return cityMapper.toDto(city);
    }

    @Override
    @Transactional
    public CityDto addCity(CityDto cityDto) {
        log.debug("Add new city");

        City city = cityMapper.toNewEntity(cityDto);
        City saved = cityRepository.save(city);

        return cityMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CityDto updateCity(Long id, CityDto cityDto) {
        log.debug("Update city with id: {}",id);

        City existing = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        existing.setName(cityDto.name());
        existing.setCountry(cityDto.country());
        existing.setLatitude(cityDto.latitude());
        existing.setLongitude(cityDto.longitude());
        existing.setTimezone(cityDto.timezone());

        City updated = cityRepository.save(existing);
        return cityMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteCity(Long id) {
        log.debug("Delete city with id: {}",id);

        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City not found with id: " + id);
        }

        cityRepository.deleteById(id);
    }
}
