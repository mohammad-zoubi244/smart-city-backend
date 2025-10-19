package com.mzoubi.smartcity.modules.airquality.controller;

import com.mzoubi.smartcity.common.ApiResponse;
import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;
import com.mzoubi.smartcity.modules.airquality.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/air-quality")
@RequiredArgsConstructor
public class AirQualityController {

    private final AirQualityService airQualityService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<AirQualityDto>>> getAllAirQuality() {
        List<AirQualityDto> airQualityDtoList = airQualityService.getAllAirQuality();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Get all air quality.", airQualityDtoList, HttpStatus.OK));
    }

    @GetMapping("{airQualityId}")
    public ResponseEntity<ApiResponse<AirQualityDto>> getAirQualityById(
            @PathVariable Long airQualityId) {
        AirQualityDto airQualityDto = airQualityService.getAirQualityById(airQualityId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Get air quality by id: " + airQualityId, airQualityDto, HttpStatus.OK));
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<List<AirQualityDto>>> getAirQualityByCityId(
            @PathVariable Long cityId) {
        List<AirQualityDto> airQualityDtoList = airQualityService.getAirQualityByCityId(cityId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Get air quality for the city: " + cityId, airQualityDtoList, HttpStatus.OK));
    }

    @PostMapping("/fetch/{cityId}")
    public ResponseEntity<ApiResponse<AirQualityDto>> fetchAirQualityByCityId(
            @PathVariable Long cityId) {
        AirQualityDto airQualityDto = airQualityService.fetchAndSaveAirQualityForCity(cityId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(String.format("Saved the air quality for the city: %d successfully.", cityId),
                        airQualityDto, HttpStatus.CREATED));
    }

    @DeleteMapping("/{airQualityId}")
    public ResponseEntity<ApiResponse<Void>> deleteAirQualityById(@PathVariable Long id) {
        airQualityService.deleteAirQuality(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Air quality deleted successfully", null, HttpStatus.OK));
    }

}
