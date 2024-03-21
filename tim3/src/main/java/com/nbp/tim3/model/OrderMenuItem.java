package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderMenuItem {
    int id;
    int quantity;
    MenuItem menuItem;
    Order order;
}
