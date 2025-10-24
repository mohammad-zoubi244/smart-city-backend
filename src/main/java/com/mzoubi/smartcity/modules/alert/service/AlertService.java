package com.mzoubi.smartcity.modules.alert.service;

import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import com.mzoubi.smartcity.modules.alert.dto.AlertDto;
import com.mzoubi.smartcity.modules.alert.entity.Alert;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.weather.entity.Weather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AlertService {
    Page<AlertDto> getAllAlerts(PageRequest pageRequest);
    Page<AlertDto> getAlertsByCity(Long cityId, PageRequest pageRequest);
    Page<AlertDto> getActiveAlerts(PageRequest pageRequest);
    Page<AlertDto> getResolvedAlerts(PageRequest pageRequest);
    void resolveAlert(Long alertId);
    void evaluateAndCreatedAlerts(City city, Weather weather, AirQuality airQuality);
    void sendAlertNotification(Alert alert);

}
