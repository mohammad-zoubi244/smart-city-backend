package com.mzoubi.smartcity.modules.alert.rules;

import com.mzoubi.smartcity.modules.airquality.entity.AirQuality;
import com.mzoubi.smartcity.modules.alert.entity.Alert;
import com.mzoubi.smartcity.modules.city.entity.City;
import com.mzoubi.smartcity.modules.weather.entity.Weather;

import java.util.List;
import java.util.Optional;

public interface AlertRule {
    List<Alert> evaluate(City city, Weather weather, AirQuality airQuality);
}
