package com.mzoubi.smartcity.modules.alert.messages;

public class AlertMessages {
    private AlertMessages() {}

    //Alert Controller messages
    public static final String GET_ALL_ALERTS_MESSAGE = "Get all alerts.";
    public static final String GET_ALERTS_BY_CITY_MESSAGE = "Get all alerts for city: ";
    public static final String GET_RESOLVED_ALERTS_MESSAGE = "Get all resolved alerts.";
    public static final String GET_ACTIVE_ALERTS_MESSAGE = "Get all active alerts.";
    public static final String RESOLVE_ALERT_MESSAGE = "Alert successfully resolved.";

    //Weather alert rule messages
    public static final String HIGH_TEMPERATURE_MESSAGE = "High temperature detected";
    public static final String LOW_TEMPERATURE_MESSAGE = "Low temperature detected";

    //Air Quality alert rule messages
    public static final String HIGH_PM2_MESSAGE = "High PM2.5 level";

}
