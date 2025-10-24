package com.mzoubi.smartcity.modules.alert.entity;

import com.mzoubi.smartcity.modules.alert.enums.AlertTypeEnum;
import com.mzoubi.smartcity.modules.alert.enums.SeverityLevelEnum;
import com.mzoubi.smartcity.modules.city.entity.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertTypeEnum type;

    @Column(nullable = false, length = 255)
    private String message;

    private Double value;

    private Double threshold;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    private Boolean resolved;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Enumerated(EnumType.STRING)
    private SeverityLevelEnum severity;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }

        resolved = false;
    }
}
