package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Restaurant {
    int id;
    String name;
    String logo;
    Address address;
    User manager;
}
