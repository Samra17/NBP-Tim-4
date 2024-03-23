package com.nbp.tim3.dto.restaurant;

import com.nbp.tim3.model.Restaurant;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

public class RestaurantShortResponse {

    private Long id;

    private String uuid;

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


    public RestaurantShortResponse(Long id, String uuid, String name, String address, String logo, boolean open,String mapCoordinates, Set<String> categories, Double rating, Integer customersRated, Integer customersFavorited,Boolean customerFavorite) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.address = address;
        this.logo = logo;
        this.open = open;
        this.mapCoordinates=mapCoordinates;
        this.categories = categories;
        this.rating = rating;
        this.customersRated = customersRated;
        this.customersFavorited = customersFavorited;
        this.customerFavorite = customerFavorite;
    }

    public RestaurantShortResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getMapCoordinates() {
        return mapCoordinates;
    }

    public void setMapCoordinates(String mapCoordinates) {
        this.mapCoordinates = mapCoordinates;
    }

    public Integer getCustomersRated() {
        return customersRated;
    }

    public void setCustomersRated(Integer customersRated) {
        this.customersRated = customersRated;
    }

    public Integer getCustomersFavorited() {
        return customersFavorited;
    }

    public void setCustomersFavorited(Integer customersFavorited) {
        this.customersFavorited = customersFavorited;
    }

    public Boolean getCustomerFavorite() {
        return customerFavorite;
    }

    public void setCustomerFavorite(Boolean customerFavorite) {
        this.customerFavorite = customerFavorite;
    }
}