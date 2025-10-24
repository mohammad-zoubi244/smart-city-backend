package com.mzoubi.smartcity.modules.alert.rules;

import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import com.mzoubi.smartcity.modules.alert.entity.Alert;
import com.mzoubi.smartcity.modules.alert.enums.AlertTypeEnum;
import com.mzoubi.smartcity.modules.alert.enums.SeverityLevelEnum;
import com.mzoubi.smartcity.modules.alert.messages.AlertMessages;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.weather.entity.Weather;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.ToDoubleFunction;

@Component
public class WeatherRule implements AlertRule {

    //Alert Limit
    private static final Double HIGH_TEMPERATURE_ALERT_LIMIT = 40.0;
    private static final Double LOW_TEMPERATURE_ALERT_LIMIT = -5.0;

    //Rule Definition
    private static final List<SubRule> SUB_RULES = List.of(
            new SubRule(
                    AlertMessages.HIGH_TEMPERATURE_MESSAGE,
                    AlertTypeEnum.WEATHER,
                    HIGH_TEMPERATURE_ALERT_LIMIT,
                    SeverityLevelEnum.WARNING,
                    Weather::getTemperature,
                    true),
            new SubRule(AlertMessages.LOW_TEMPERATURE_MESSAGE,
                    AlertTypeEnum.WEATHER,
                    LOW_TEMPERATURE_ALERT_LIMIT,
                    SeverityLevelEnum.WARNING,
                    Weather::getTemperature,
                    false)
    );

    @Override
    public List<Alert> evaluate(City city, Weather weather, AirQuality airQuality) {
        if (weather == null) return List.of();
        return SUB_RULES.stream()
                .filter(rule -> {
                    double value = rule.metricExtractor().applyAsDouble(weather);
                    return rule.greaterThan() ? value > rule.threshold() : value < rule.threshold();
                })
                .map(rule -> Alert.builder()
                        .city(city)
                        .type(rule.type)
                        .message(rule.message)
                        .value(rule.metricExtractor().applyAsDouble(weather))
                        .threshold(rule.threshold)
                        .severity(rule.severity)
                        .build())
                .toList();
    }

    private record SubRule(
            String message,
            AlertTypeEnum type,
            double threshold,
            SeverityLevelEnum severity,
            ToDoubleFunction<Weather> metricExtractor,
            boolean greaterThan  // true if alert triggers above threshold, false if below
    ) {
    }
}
