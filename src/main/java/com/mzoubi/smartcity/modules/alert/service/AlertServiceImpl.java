package com.mzoubi.smartcity.modules.alert.service;

import com.mzoubi.smartcity.common.exceptions.ResourceNotFoundException;
import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import com.mzoubi.smartcity.modules.alert.dto.AlertDto;
import com.mzoubi.smartcity.modules.alert.entity.Alert;
import com.mzoubi.smartcity.modules.alert.mapper.AlertMapper;
import com.mzoubi.smartcity.modules.alert.notification.AlertNotificationService;
import com.mzoubi.smartcity.modules.alert.repoistory.AlertRepository;
import com.mzoubi.smartcity.modules.alert.rules.AlertRule;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.weather.entity.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final AlertNotificationService alertNotificationService;
    private final List<AlertRule> rules;

    @Override
    public Page<AlertDto> getAllAlerts(PageRequest pageRequest) {
        log.debug("Get all alerts.");
        return alertRepository.findAll(pageRequest).map(alertMapper::toDto);
    }

    @Override
    public Page<AlertDto> getAlertsByCity(Long cityId, PageRequest pageRequest) {
        log.debug("Get alerts for city with id: {}", cityId);
        return alertRepository.findByCityId(cityId, pageRequest).map(alertMapper::toDto);
    }

    @Override
    public Page<AlertDto> getActiveAlerts(PageRequest pageRequest) {
        log.debug("Get active alerts");
        return alertRepository.findByResolvedFalse(pageRequest).map(alertMapper::toDto);
    }

    @Override
    public Page<AlertDto> getResolvedAlerts(PageRequest pageRequest) {
        log.debug("Get resolved alerts");
        return alertRepository.findByResolvedTrue(pageRequest).map(alertMapper::toDto);
    }

    @Override
    public void resolveAlert(Long alertId) {
        log.debug("Resolve alerts");
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + alertId));
        alert.setResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }

    @Override
    public void evaluateAndCreatedAlerts(City city, Weather weather, AirQuality airQuality) {
        log.debug("Evaluate and created alerts");
        rules.forEach(rule -> {
            List<Alert> alerts = rule.evaluate(city, weather, airQuality);
            alerts.forEach(alert -> {
                alertRepository.save(alert);
                sendAlertNotification(alert);
            });
        });
    }

    @Override
    public void sendAlertNotification(Alert alert) {
        log.debug("Send alert notification");
        alertNotificationService.sendAlertNotification(alert);
    }

}
