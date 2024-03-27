package com.nbp.tim3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    int id;
    String street;
    String municipality;

    String mapCoordinates;
}
