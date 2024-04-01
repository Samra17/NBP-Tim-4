package com.nbp.tim3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    int id;
    String name;
    String description;
    Double price;
    Double discountPrice;
    String image;
    Integer prepTime;
    int menuId;
}
