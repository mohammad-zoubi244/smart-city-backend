package com.mzoubi.smartcity.modules.airquality.controller;

import com.mzoubi.smartcity.common.dto.ApiResponse;
import com.mzoubi.smartcity.common.dto.PagedResponse;
import com.mzoubi.smartcity.common.utils.PaginationUtils;
import com.mzoubi.smartcity.modules.airquality.dto.AirQualityDto;
import com.mzoubi.smartcity.modules.airquality.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/air-quality")
@RequiredArgsConstructor
public class AirQualityController {

    private static final Set<String> ALLOWED_FIELDS = Set.of("id");

    private final AirQualityService airQualityService;

    @GetMapping()
    public ResponseEntity<ApiResponse<PagedResponse<AirQualityDto>>> getAllAirQuality(
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<AirQualityDto> airQualityDtoList = PagedResponse.from(
                airQualityService.getAllAirQuality(pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Get all air quality.",
                        airQualityDtoList, HttpStatus.OK));
    }

    @GetMapping("{airQualityId}")
    public ResponseEntity<ApiResponse<AirQualityDto>> getAirQualityById(
            @PathVariable Long airQualityId) {
        AirQualityDto airQualityDto = airQualityService.getAirQualityById(airQualityId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Get air quality by id: " + airQualityId,
                        airQualityDto, HttpStatus.OK));
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<PagedResponse<AirQualityDto>>> getAirQualityByCityId(
            @PathVariable Long cityId,
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<AirQualityDto> airQualityDtoList = PagedResponse.from(
                airQualityService.getAirQualityByCityId(cityId, pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Get air quality for the city: " + cityId,
                        airQualityDtoList, HttpStatus.OK));
    }

    @PostMapping("/fetch/{cityId}")
    public ResponseEntity<ApiResponse<AirQualityDto>> fetchAirQualityByCityId(
            @PathVariable Long cityId) {
        AirQualityDto airQualityDto = airQualityService.fetchAndSaveAirQualityForCity(cityId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        String.format("Saved the air quality for the city: %d successfully.", cityId),
                        airQualityDto, HttpStatus.CREATED));
    }

    @DeleteMapping("/{airQualityId}")
    public ResponseEntity<ApiResponse<Void>> deleteAirQualityById(@PathVariable Long id) {
        airQualityService.deleteAirQuality(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Air quality deleted successfully",
                        null,
                        HttpStatus.OK));
    }

}
