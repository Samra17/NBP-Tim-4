package com.nbp.tim3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    int id;
    String name;
    String logo;
    Address address;
    User manager;
    List<OpeningHours> openingHours;
    List<Category> categories;
}
