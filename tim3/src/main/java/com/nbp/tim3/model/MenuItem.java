package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuItem {
    int id;
    String name;
    String description;
    float price;
    float discountPrice;
    String image;
    int prepTime;
    Menu menu;
}
