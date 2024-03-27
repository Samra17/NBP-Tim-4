package com.nbp.tim3.dto.restaurant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCreateRequest implements Serializable {
    @NotNull(message = "Restaurant name must be specified!")
    @Size(min=3,max=80,message = "Restaurant name must be between 3 and 80 characters!")
    private String name;

    @NotNull(message = "Restaurant manager id must be specified!")
    private int managerId;

    @Size(min=3,max=80,message = "Restaurant address must be between 3 and 80 characters!")
    @NotNull(message = "Restaurant address must be specified!")
    private String address;

    @Size(min=3,max=80,message = "Restaurant city must be between 3 and 80 characters!")
    @NotNull(message = "Restaurant city must be specified!")
    private String city;

    @Pattern(regexp ="^((-)?[0-9]?[0-9]\\.\\d+,(\\s)*(-)?[1]?[0-9]?[0-9]\\.\\d+)",message = "Map coordinates must represent latitude and longitude values!")
    @NotNull(message = "Map coordinates must be specified!")
    private String mapCoordinates;


}
