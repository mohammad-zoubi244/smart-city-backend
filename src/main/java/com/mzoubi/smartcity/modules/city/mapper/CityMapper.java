package com.mzoubi.smartcity.modules.city.mapper;

import com.mzoubi.smartcity.modules.city.dto.CityDto;
import com.mzoubi.smartcity.modules.city.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityDto toDto(City city);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    City toEntity(CityDto dto);
}
