package com.restaurant.booking.model.entity;

import com.restaurant.booking.model.dto.LocationMetric;
import com.restaurant.booking.model.dto.StaffMetric;
import com.restaurant.booking.util.LocationMetricListConverter;
import com.restaurant.booking.util.StaffMetricListConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reports")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reports {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "location_id")
    private String locationId;

    @Convert(converter = StaffMetricListConverter.class)
    @Column(name = "staff_metrics", columnDefinition = "TEXT")
    private List<StaffMetric> staffMetrics;

    @Convert(converter = LocationMetricListConverter.class)
    @Column(name = "location_metrics", columnDefinition = "TEXT")
    private List<LocationMetric> locationMetrics;

    @Column(name = "download_link")
    private String downloadLink;
}