package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class OpeningHours {
    int id;
    String dayOfWeek;
    LocalTime openingTime;
    LocalTime closingTime;
}
