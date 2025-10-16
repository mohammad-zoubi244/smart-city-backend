package com.mzoubi.smartcity.modules.weather.controller;

import com.mzoubi.smartcity.common.ApiResponse;
import com.mzoubi.smartcity.modules.weather.dto.WeatherDto;
import com.mzoubi.smartcity.modules.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WeatherDto>>> getAllWeather() {
        List<WeatherDto> weatherList = weatherService.getAllWeather();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Fetched all weather", weatherList, HttpStatus.OK));
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<List<WeatherDto>>> getWeatherByCity(@PathVariable Long cityId) {
        List<WeatherDto> cityWeather = weatherService.getWeatherByCityId(cityId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Fetched the weather for city: " + cityId, cityWeather, HttpStatus.OK));
    }

    @PostMapping("/fetch/{cityId}")
    public ResponseEntity<ApiResponse<WeatherDto>> fetchWeatherForCity(@PathVariable Long cityId) {
        WeatherDto fetchedWeather = weatherService.fetchAndSaveWeatherForCity(cityId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Fetched the weather for city: " + cityId, fetchedWeather, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWeather(@PathVariable Long id) {
        weatherService.deleteWeather(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Weather deleted successfully",null, HttpStatus.OK));
    }
}
