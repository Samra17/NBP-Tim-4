package com.nbp.tim3.dto.restaurant;

import com.nbp.tim3.dto.address.AddressResponse;
import com.nbp.tim3.dto.category.CategoryResponse;
import com.nbp.tim3.dto.openinghours.OpeningHoursResponse;
import com.nbp.tim3.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponse {

    private int id;

    private String name;

    private AddressResponse address;

    private String logo;
    private int managerId;

    private OpeningHoursResponse openingHours;

    private List<CategoryResponse> categories;
    private Double rating;

    private Integer customersRated;
    private Integer customersFavorited;

    public RestaurantResponse(Restaurant restaurant, Number rating, Number customersRated, Number customersFavorited) {
        this.id= restaurant.getId();
        this.name=restaurant.getName();
        this.managerId = restaurant.getManager().getId();

        if(restaurant.getAddress() != null)
            this.address = new AddressResponse(restaurant.getAddress());


        if(restaurant.getOpeningHours() != null)
            this.openingHours = new OpeningHoursResponse(restaurant.getOpeningHours());

        this.logo=restaurant.getLogo();
        this.rating=rating.doubleValue();
        this.customersFavorited = customersFavorited.intValue();
        this.customersRated = customersRated.intValue();

        if(restaurant.getCategories() != null)
            this.categories= restaurant.getCategories().stream().map(CategoryResponse::new).collect(Collectors.toList());
        else
            this.categories = new ArrayList<>();

    }

    public RestaurantResponse(Restaurant restaurant) {
        this.id= restaurant.getId();
        this.name=restaurant.getName();
        this.logo=restaurant.getLogo();

        if(restaurant.getManager() != null)
            this.managerId= restaurant.getManager().getId();

        if(restaurant.getAddress() != null)
            this.address = new AddressResponse(restaurant.getAddress());

        if( restaurant.getOpeningHours() != null)
            this.openingHours = new OpeningHoursResponse(restaurant.getOpeningHours());

        if(restaurant.getCategories() != null)
            this.categories= restaurant.getCategories().stream().map(CategoryResponse::new).collect(Collectors.toList());
        else
            this.categories = new ArrayList<>();


    }



}