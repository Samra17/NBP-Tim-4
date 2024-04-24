package com.nbp.tim3.service;

import com.nbp.tim3.dto.pagination.PaginatedRequest;
import com.nbp.tim3.dto.restaurant.FavoriteRestaurantResponse;
import com.nbp.tim3.dto.restaurant.RestaurantPaginatedResponse;
import com.nbp.tim3.dto.restaurant.RestaurantPaginatedShortResponse;
import com.nbp.tim3.dto.restaurant.RestaurantShortResponse;
import com.nbp.tim3.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public FavoriteRestaurantResponse addRestaurantToFavorites(int restaurantId, String username) {

        var favoriteRestaurant = restaurantRepository.addRestaurantToFavorites(restaurantId,username);


        return favoriteRestaurant;
    }

    public int removeRestaurantFromFavorites(int restaurantId, String username)  {
        return restaurantRepository.removeRestaurantFromFavorites(restaurantId, username);
    }
}
