package com.mzoubi.smartcity.modules.airquality.entity;

import com.mzoubi.smartcity.modules.city.entity.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "air_quality")
public class AirQuality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    // Air quality metrics
    @Column(name = "aqi")
    private Integer aqi;

    @Column(name = "pm2_5")
    private Double pm25;

    @Column(name = "pm10")
    private Double pm10;

    @Column(name = "no2")
    private Double no2;

    @Column(name = "o3")
    private Double o3;

    @Column(name = "co")
    private Double co;

    @Column(name = "so2")
    private Double so2;

    // Timestamp for the measurement
    private LocalDateTime timestamp;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
