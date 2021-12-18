package kh.com.sathapana.store.service.entity;

import lombok.*;

import java.time.DayOfWeek;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHour {
    String startTime;
    String endTime;
    DayOfWeek dayOfWeek;
}