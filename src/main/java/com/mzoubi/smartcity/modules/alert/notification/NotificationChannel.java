package com.mzoubi.smartcity.modules.alert.notification;

import com.mzoubi.smartcity.modules.alert.entity.Alert;

public interface NotificationChannel {
    void send(Alert alert);
}
