package com.mzoubi.smartcity.modules.alert.notification;

import com.mzoubi.smartcity.modules.alert.entity.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertNotificationService {

    private final List<NotificationChannel> channels;

    public void sendAlertNotification(Alert alert) {
        log.info("🚨 New Alert [{}]: {}", alert.getType(), alert.getMessage());
        // Send alert through all registered channels - uncomment the following line
        //channels.forEach(channel -> channel.send(alert));
    }
}
