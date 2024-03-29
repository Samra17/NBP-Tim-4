package com.nbp.tim3.service;

import com.nbp.tim3.dto.openinghours.OpeningHoursCreateRequest;
import com.nbp.tim3.dto.restaurant.*;
import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.repository.CategoryRepository;
import com.nbp.tim3.repository.RestaurantRepository;
import com.nbp.tim3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    /*
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FavoriteRestaurantRepository favoriteRestaurantRepository;

     */


    public RestaurantResponse addNewRestaurant(RestaurantCreateRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        Address address = new Address(0,request.getAddress(),request.getCity(),request.getMapCoordinates());

        restaurantRepository.addRestaurant(restaurant,address,request.getManagerId());

        restaurant.setAddress(address);
        RestaurantResponse response =  new RestaurantResponse(restaurant);
        response.setManagerId(request.getManagerId());

        return response;

    }

    public Restaurant updateRestaurant(RestaurantUpdateRequest request, Long id, String uuid) {
        /*var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id).orElseThrow(()-> exception);
        restaurant.setName(request.getName());
        restaurant.setMapCoordinates(request.getMapCoordinates());
        restaurant.setAddress(request.getAddress());
        restaurant.setLogo(request.getLogo());
        restaurant.setModified(LocalDateTime.now());
        restaurant.setModifiedBy(uuid);
        restaurantRepository.save(restaurant);
        return restaurant;*/

        return new Restaurant();

    }


    public List<RestaurantShortResponse> searchForRestaurants(FilterRestaurantRequest filterRestaurantRequest, String sortBy, Boolean ascending) {
        //return restaurantRepository.getRestaurants(filterRestaurantRequest,sortBy,ascending);

        return new ArrayList<>();
    }

    public List<RestaurantResponse> getFullRestaurants() {
        // return restaurantRepository.getFullRestaurants();

        return new ArrayList<>();
    }

    public RestaurantShortResponse getRestaurantById(Long id, String customerUUID) {
        /*var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        try {
            var restaurant = restaurantRepository.getRestaurantShortResponseById(id);
            System.out.println(restaurant);
            restaurant.setCustomerFavorite(restaurantRepository.checkIfRestaurantIsCustomersFavorite(id,customerUUID));
            return restaurant;
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }*/

        return new RestaurantShortResponse();
    }

    public RestaurantResponse getRestaurantByManagerUUID(String managerUUID) {
        /*var exception = new EntityNotFoundException("Restaurant with manager UUID " + managerUUID + " does not exist!");
        try {
            var restaurant = restaurantRepository.getRestaurantByManagerUUID(managerUUID);
            return restaurant;
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }*/

        return new RestaurantResponse();

    }

    public String getRestaurantUUIDByManagerUUID(String managerUUID) {
        /*var exception = new EntityNotFoundException("Restaurant with manager UUID " + managerUUID + " does not exist!");
        try {
            return restaurantRepository.getRestaurantUUIDByManagerUUID(managerUUID);
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }*/

        return "Something";
    }

    public RestaurantResponse getRestaurantFullResponseById(Long id) {
        /*var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        try {
            return restaurantRepository.getRestaurantFullResponseById(id);
        } catch(Exception e) {
            throw exception;
        }*/

        return new RestaurantResponse();

    }

    public List<Restaurant> getRestaurantsWithCategories(List<Long> categoryIds) {
        // return categoryRepository.getRestaurantsWithCategories(categoryIds);
        return new ArrayList<>();
    }

    public String deleteRestaurant(Long id) {
        /*var restaurant = restaurantRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Restaurant with id " + id + " does not exist!"));
        restaurantRepository.delete(restaurant);
        return "Restaurant with id " + id + " successfully deleted!";*/

        return "Something";
    }

    public Restaurant addCategoriesToRestaurant(Long id, List<Long> categoryIds,String userUUID) {
        /*var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->exception);
        if(restaurant.getCategories().stream().map(c -> c.getId()).collect(Collectors.toList()).equals(categoryIds)) {
            System.out.println("all match");
            return restaurant;
        }
        var categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        restaurant.setCategories(categories);
        restaurant.setModified(LocalDateTime.now());
        restaurant.setModifiedBy(userUUID);
        restaurantRepository.save(restaurant);
        return restaurant;*/

        return new Restaurant();
    }

    public Restaurant setRestaurantOpeningHours(Long id, OpeningHoursCreateRequest request, String userUUID) {
        /*var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var restaurant = restaurantRepository.findById(id).orElseThrow(()->exception);

        var created = userUUID;
        var openingHours = new OpeningHours(request.getMondayOpen(),
                request.getMondayClose(),
                request.getTuesdayOpen(),
                request.getTuesdayClose(),
                request.getWednesdayOpen(),
                request.getWednesdayClose(),
                request.getThursdayOpen(),
                request.getThursdayClose(),
                request.getFridayOpen(),
                request.getFridayClose(),
                request.getSaturdayOpen(),
                request.getSaturdayClose(),
                request.getSundayOpen(),
                request.getSundayClose(),
                LocalDateTime.now(),
                created);

        restaurant.setOpeningHours(openingHours);
        restaurant.setModified(LocalDateTime.now());
        restaurant.setModifiedBy(created);
        restaurantRepository.save(restaurant);
        return restaurant;*/

        return new Restaurant();
    }

    public Double calculateAverageRatingForRestaurant(Long restaurantId) {
        /*var exception = new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!");
        restaurantRepository.findById(restaurantId).orElseThrow(()->exception);
        return reviewRepository.calculateAverageRatingForRestaurant(restaurantId);*/

        return 3.0;
    }

    public String getRestaurantUUID(Long id) {
        /*var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        var uuid= restaurantRepository.getRestaurantUUID(id);
        if(uuid==null)
            throw exception;
        return uuid;*/

        return "Something";
    }


    public Long getCustomersFavorited(String restaurantUUID) {
        /*restaurantRepository.findByUUID(restaurantUUID).orElseThrow();
        return favoriteRestaurantRepository.countNumberOfFavorites(restaurantUUID);*/

        return 3L;
    }
}
