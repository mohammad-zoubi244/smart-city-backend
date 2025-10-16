package com.mzoubi.smartcity.modules.weather.mapper;

import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;
import com.mzoubi.smartcity.modules.weather.entity.Weather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeatherMapper {

    @Mapping(target = "cityName", source = "city.name")
    WeatherDto toDto(Weather weather);

    @Mapping(target = "id", ignore = true)
    Weather toNewEntity(WeatherDto dto);

    @Mapping(target = "city", ignore = true)
    Weather toExistingEntity(WeatherDto dto);
}
