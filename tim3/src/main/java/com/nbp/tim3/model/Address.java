package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
    int id;
    String street;
    String municipality;

    String mapCoordinates;
}
