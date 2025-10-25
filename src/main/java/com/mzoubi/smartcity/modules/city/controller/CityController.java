package com.mzoubi.smartcity.modules.city.controller;

import com.mzoubi.smartcity.common.dto.ApiResponse;
import com.mzoubi.smartcity.common.dto.PagedResponse;
import com.mzoubi.smartcity.common.utils.PaginationUtils;
import com.mzoubi.smartcity.modules.city.dto.CityDto;
import com.mzoubi.smartcity.modules.city.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/city")
@RequiredArgsConstructor
public class CityController {

    private static final Set<String> ALLOWED_FIELDS = Set.of("id");

    private final CityService cityService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CityDto>>> getAllCities(
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<CityDto> cities = PagedResponse.from(cityService.getAllCities(pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Fetched all cities", cities, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CityDto>> getCity(
            @PathVariable Long id) {
        CityDto city = cityService.getCityById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Fetched city " + id, city, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CityDto>> createCity(
            @RequestBody CityDto dto) {
        CityDto created = cityService.addCity(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "City created successfully", created, HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CityDto>> updateCity(
            @PathVariable Long id, @RequestBody CityDto dto) {
        CityDto created = cityService.updateCity(id, dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "City updated successfully", created, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCity(
            @PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "City deleted successfully", null, HttpStatus.OK));
    }
}
