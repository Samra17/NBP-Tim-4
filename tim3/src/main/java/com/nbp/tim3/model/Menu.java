package com.nbp.tim3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    int id;
    String name;
    Boolean active;
    int restaurantId;
    List<MenuItem> menuItems;
}
