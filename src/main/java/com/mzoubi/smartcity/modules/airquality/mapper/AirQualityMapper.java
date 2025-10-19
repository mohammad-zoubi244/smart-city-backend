package com.mzoubi.smartcity.modules.airquality.mapper;

import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;
import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirQualityMapper {

    @Mapping(target = "cityName", source = "city.name")
    AirQualityDto toDto(AirQuality airQuality);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AirQuality toNewEntity(AirQualityDto dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AirQuality toExistingEntity(AirQualityDto dto);
}
