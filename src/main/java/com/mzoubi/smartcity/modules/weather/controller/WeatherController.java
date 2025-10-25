package com.mzoubi.smartcity.modules.weather.controller;

import com.mzoubi.smartcity.common.dto.ApiResponse;
import com.mzoubi.smartcity.common.dto.PagedResponse;
import com.mzoubi.smartcity.common.utils.PaginationUtils;
import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;
import com.mzoubi.smartcity.modules.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private static final Set<String> ALLOWED_FIELDS = Set.of("id");

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<WeatherDto>>> getAllWeather(
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<WeatherDto> weatherList = PagedResponse.from(
                weatherService.getAllWeather(pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Fetched all weather", weatherList, HttpStatus.OK));
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<PagedResponse<WeatherDto>>> getWeatherByCity(
            @PathVariable Long cityId,
            @RequestParam(value = "page-size", required = false) final Integer pageSize,
            @RequestParam(value = "page-number", required = false) final Integer pageNumber,
            @RequestParam(value = "sort-by", required = false) String sortBy,
            @RequestParam(value = "sort-direction", required = false) String sortDirection) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(
                pageNumber, pageSize, sortBy, sortDirection, ALLOWED_FIELDS);
        PagedResponse<WeatherDto> cityWeather = PagedResponse.from(
                weatherService.getWeatherByCityId(cityId, pageRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Fetched the weather for city: " + cityId, cityWeather, HttpStatus.OK));
    }

    @PostMapping("/fetch/{cityId}")
    public ResponseEntity<ApiResponse<WeatherDto>> fetchWeatherForCity(
            @PathVariable Long cityId) {
        WeatherDto fetchedWeather = weatherService.fetchAndSaveWeatherForCity(cityId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(String.format(
                                "Saved the weather for the city: %d successfully.", cityId),
                        fetchedWeather, HttpStatus.CREATED));

    }

    @DeleteMapping("/{weatherId}")
    public ResponseEntity<ApiResponse<Void>> deleteWeatherByWeatherId(
            @PathVariable Long id) {
        weatherService.deleteWeather(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Weather deleted successfully", null, HttpStatus.OK));
    }
}
