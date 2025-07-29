package com.epam.edp.demo.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private String date;
    private String startTime;
    private String endTime;
    private boolean isReserved;
}
