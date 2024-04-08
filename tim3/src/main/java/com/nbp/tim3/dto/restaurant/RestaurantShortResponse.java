package com.nbp.tim3.dto.restaurant;

import com.nbp.tim3.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantShortResponse {

    private Long id;

    private String name;

    private String address;


    private String logo;

    private boolean open;

    String mapCoordinates;

    Set<String> categories;
    Double rating;

    Integer customersRated;
    Integer customersFavorited;

    Boolean customerFavorite;

    public RestaurantShortResponse(Restaurant restaurant, Double rating, Number customersRated, Number customersFavorited) {
        /*this.id= restaurant.getId();
        this.name=restaurant.getName();
        this.uuid = restaurant.getUuid();
        this.address=restaurant.getAddress();
        this.mapCoordinates = restaurant.getMapCoordinates();
        this.logo=restaurant.getLogo();
        this.rating=rating;
        this.customersFavorited = customersFavorited.intValue();
        this.customersRated = customersRated.intValue();
        this.categories=restaurant.getCategories().stream().map(c->c.getName()).collect(Collectors.toSet());

        if(restaurant.getOpeningHours()!=null) {
            var day = LocalDateTime.now().getDayOfWeek();

            switch (day) {
                case MONDAY -> {
                    if(restaurant.getOpeningHours().getMondayOpen()==null)
                    {
                        this.open=false;
                        break;
                    }
                    this.open = restaurant.getOpeningHours().getMondayOpen().isBefore(LocalTime.now())
                            &&  restaurant.getOpeningHours().getMondayClose().isAfter(LocalTime.now());
                    break;
                }

                case TUESDAY -> {
                    if(restaurant.getOpeningHours().getTuesdayOpen()==null)
                    {
                        this.open=false;
                        break;
                    }
                    this.open = restaurant.getOpeningHours().getTuesdayOpen().isBefore(LocalTime.now())
                            &&  restaurant.getOpeningHours().getTuesdayClose().isAfter(LocalTime.now());
                    break;
                }

                case WEDNESDAY -> {
                    if(restaurant.getOpeningHours().getWednesdayOpen()==null)
                    {
                        this.open=false;
                        break;
                    }
                    this.open = restaurant.getOpeningHours().getWednesdayOpen().isBefore(LocalTime.now())
                            &&  restaurant.getOpeningHours().getWednesdayClose().isAfter(LocalTime.now());
                    break;
                }
                case THURSDAY -> {
                    if(restaurant.getOpeningHours().getThursdayOpen()==null)
                    {
                        this.open=false;
                        break;
                    }
                    this.open = restaurant.getOpeningHours().getThursdayOpen().isBefore(LocalTime.now())
                            &&  restaurant.getOpeningHours().getThursdayClose().isAfter(LocalTime.now());
                    break;
                }

                case FRIDAY -> {
                    if(restaurant.getOpeningHours().getFridayOpen()==null)
                    {
                        this.open=false;
                        break;
                    }
                    this.open = restaurant.getOpeningHours().getFridayOpen().isBefore(LocalTime.now())
                            &&  restaurant.getOpeningHours().getFridayClose().isAfter(LocalTime.now());
                    break;
                }

                case SATURDAY -> {
                    if(restaurant.getOpeningHours().getSaturdayOpen()==null)
                    {
                        this.open=false;
                        break;
                    }
                    this.open = restaurant.getOpeningHours().getSaturdayOpen().isBefore(LocalTime.now())
                            &&  restaurant.getOpeningHours().getSaturdayClose().isAfter(LocalTime.now());
                    break;
                }

                case SUNDAY -> {
                    if(restaurant.getOpeningHours().getSundayOpen()==null)
                    {
                        this.open=false;
                        break;
                    }
                    this.open = restaurant.getOpeningHours().getSundayOpen().isBefore(LocalTime.now())
                            &&  restaurant.getOpeningHours().getSundayClose().isAfter(LocalTime.now());
                    break;
                }
            } } else {
            this.open=false;
        }*/
    }



}