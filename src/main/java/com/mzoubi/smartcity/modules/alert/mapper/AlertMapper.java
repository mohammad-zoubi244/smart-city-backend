package com.mzoubi.smartcity.modules.alert.mapper;

import com.mzoubi.smartcity.modules.alert.dto.AlertDto;
import com.mzoubi.smartcity.modules.alert.entity.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlertMapper {

    @Mapping(target = "cityName", source = "city.name")
    AlertDto toDto(Alert alert);
    Alert toEntity(AlertDto alertDto);
}
