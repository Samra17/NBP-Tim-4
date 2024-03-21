package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Menu {
    int id;
    String name;
    Boolean active;
    Restaurant restaurant;
}
