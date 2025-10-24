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
public class AirQualityRule implements AlertRule {

    //Alert Limit
    private static final Double PM2_ALERT_LIMIT = 100.0;

    //Rule Definition
    private static final List<SubRule> SUB_RULES = List.of(
            new SubRule(
                    AlertMessages.HIGH_PM2_MESSAGE,
                    AlertTypeEnum.AIR_QUALITY,
                    PM2_ALERT_LIMIT,
                    SeverityLevelEnum.CRITICAL,
                    AirQuality::getPm25)
    );

    @Override
    public List<Alert> evaluate(City city, Weather weather, AirQuality airQuality) {
        if (airQuality == null) return List.of();

        return SUB_RULES.stream()
                .filter(rule -> rule.metricExtractor().applyAsDouble(airQuality) > rule.threshold())
                .map(rule -> Alert.builder()
                        .city(city)
                        .type(rule.type)
                        .message(rule.message)
                        .value(rule.metricExtractor().applyAsDouble(airQuality))
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
            ToDoubleFunction<AirQuality> metricExtractor
    ) {
    }
}

