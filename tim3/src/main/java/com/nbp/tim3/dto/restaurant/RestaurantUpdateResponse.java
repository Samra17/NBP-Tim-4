package com.nbp.tim3.dto.restaurant;


import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateResponse {
    int id;
    String name;
    String logo;
    Address address;

    public RestaurantUpdateResponse(Restaurant r) {
        address = r.getAddress();
        name = r.getName();
        logo = r.getLogo();
        id = r.getId();
    }
}
