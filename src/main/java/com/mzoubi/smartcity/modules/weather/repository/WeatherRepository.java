package com.mzoubi.smartcity.modules.weather.repository;

import com.mzoubi.smartcity.modules.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather,Long> {

    List<Weather> findByCityId(Long cityId);
}
