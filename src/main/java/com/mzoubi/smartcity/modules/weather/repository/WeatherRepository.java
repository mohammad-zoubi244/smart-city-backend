package com.mzoubi.smartcity.modules.weather.repository;

import com.mzoubi.smartcity.modules.weather.entity.Weather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather,Long> {

    Page<Weather> findByCityId(Long cityId, PageRequest pageRequest);
}
