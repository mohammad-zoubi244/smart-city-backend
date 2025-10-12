package com.mzoubi.smartcity.modules.city.repository;

import com.mzoubi.smartcity.modules.city.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Long> {

    Optional<City> findByNameIgnoreCase(String name);
}
