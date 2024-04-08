package com.nbp.tim3.dto.restaurant;

import com.nbp.tim3.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCreateResponse extends RestaurantUpdateResponse{
    int managerId;

    public RestaurantCreateResponse(Restaurant r, int managerId) {
        address = r.getAddress();
        this.managerId = managerId;
        name = r.getName();
        logo = r.getLogo();
        id = r.getId();
    }
}
