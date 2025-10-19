package com.mzoubi.smartcity.modules.airquality.repository;

import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirQualityRepository extends JpaRepository<AirQuality,Long> {

    List<AirQuality> findAirQualityByCityId(Long cityId);
}
