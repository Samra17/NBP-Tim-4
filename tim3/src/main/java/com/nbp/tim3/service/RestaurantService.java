package com.nbp.tim3.service;

import com.nbp.tim3.dto.openinghours.OpeningHoursCreateRequest;
import com.nbp.tim3.dto.pagination.PaginatedRequest;
import com.nbp.tim3.dto.pagination.PaginatedResponse;
import com.nbp.tim3.dto.restaurant.*;
import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.repository.CategoryRepository;
import com.nbp.tim3.repository.RestaurantRepository;
import com.nbp.tim3.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;



    @Autowired
    private ReviewRepository reviewRepository;

    /*
    @Autowired
    private FavoriteRestaurantRepository favoriteRestaurantRepository;

     */


    public RestaurantCreateResponse addNewRestaurant(RestaurantCreateRequest request) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        Address address = new Address(0,request.getAddress(),request.getCity(),request.getMapCoordinates());

        restaurantRepository.addRestaurant(restaurant,address,request.getManagerId());

        restaurant.setAddress(address);

        return new RestaurantCreateResponse(restaurant,request.getManagerId());

    }

    public RestaurantUpdateResponse updateRestaurant(RestaurantUpdateRequest request, int id) {
        if(!restaurantRepository.checkExists(id))
            throw new EntityNotFoundException(String.format("Restaurant with id %d does not exist!",id));

        var restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(request.getName());
        restaurant.setLogo(request.getLogo());
        Address address = new Address(0,request.getAddress(),request.getCity(),request.getMapCoordinates());
        restaurant.setAddress(address);

        restaurantRepository.updateRestaurant(restaurant,address);

        return new RestaurantUpdateResponse(restaurant);
    }


    public RestaurantPaginatedShortResponse searchForRestaurants(PaginatedRequest paginatedRequest, String username, FilterRestaurantRequest filterRestaurantRequest, String sortBy, Boolean ascending) {
        return restaurantRepository.getRestaurants(paginatedRequest,username, filterRestaurantRequest,sortBy,ascending);

    }

    public RestaurantPaginatedResponse getFullRestaurants(PaginatedRequest paginatedRequest) {
         return restaurantRepository.getFullRestaurants(paginatedRequest);
    }

    public RestaurantShortResponse getRestaurantById(int id, String customerUsername) {
        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");
        try {
            var restaurant = restaurantRepository.getRestaurantShortResponseById(id,customerUsername);

            if(restaurant==null)
                throw exception;

            return restaurant;
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }
    }

    public RestaurantResponse getRestaurantByManager(String managerUsername) {
        var exception = new EntityNotFoundException("Restaurant with manager " + managerUsername + " does not exist!");
        try {
            var restaurant = restaurantRepository.getRestaurantByManagerUsername(managerUsername);

            if(restaurant==null)
                throw exception;

            return restaurant;
        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }


    }

    public int getRestaurantIdByManager(String username) {
        var exception = new EntityNotFoundException("Restaurant with manager " + username + " does not exist!");
        try {
            Integer id = restaurantRepository.getRestaurantIdByManagerUsername(username);

            if(id==null)
                throw exception;

            return id;

        } catch(Exception e) {
            e.printStackTrace();
            throw exception;
        }
    }

    public RestaurantResponse getRestaurantFullResponseById(int id) {

        var exception = new EntityNotFoundException("Restaurant with id " + id + " does not exist!");

        RestaurantResponse restaurant = restaurantRepository.getRestaurantResponseById(id);

        if(restaurant==null)
            throw exception;

        return restaurant;

    }

    public RestaurantPaginatedResponse getRestaurantsWithCategories(PaginatedRequest paginatedRequest, List<Integer> categoryIds) {
        return restaurantRepository.getRestaurantsWithCategories(paginatedRequest,categoryIds);
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

    public Double calculateAverageRatingForRestaurant(int restaurantId) {
        var exception = new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!");
        if(!restaurantRepository.checkExists(restaurantId))
            throw exception;
        return reviewRepository.calculateAverageRatingForRestaurant(restaurantId);

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
