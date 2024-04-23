package com.nbp.tim3.service;

import com.nbp.tim3.dto.pagination.PaginatedRequest;
import com.nbp.tim3.dto.restaurant.RestaurantPaginatedResponse;
import com.nbp.tim3.dto.restaurant.RestaurantPaginatedShortResponse;
import com.nbp.tim3.dto.restaurant.RestaurantShortResponse;
import com.nbp.tim3.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteRestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public RestaurantPaginatedShortResponse getFavoriteRestaurants(PaginatedRequest paginatedRequest,String username) {
        return restaurantRepository.getFavoriteRestaurants(paginatedRequest,username);

    }

    /*public FavoriteRestaurant addRestaurantToFavorites(Long restaurantId,String userUUID) {
        FavoriteRestaurant favoriteRestaurant = new FavoriteRestaurant();
        var exception = new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!");
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> exception);
        favoriteRestaurant.setRestaurant(restaurant);
        favoriteRestaurant.setUserUUID(userUUID);
        favoriteRestaurant.setCreated(LocalDateTime.now());
        favoriteRestaurant.setCreatedBy(userUUID);
        favoriteRestaurantRepository.save(favoriteRestaurant);

        return favoriteRestaurant;
    }*/

    public void removeRestaurantFromFavorites(Long restaurantId, String userUUID)  {
        /*var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()->new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!"));
        favoriteRestaurantRepository.removeRestaurantFromFavorites(restaurantId, userUUID);*/
    }
}
