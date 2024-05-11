package com.nbp.tim3.service;

import com.nbp.tim3.dto.restaurantimage.RestaurantImageResponse;
import com.nbp.tim3.dto.restaurantimage.RestaurantImageUploadRequest;
import com.nbp.tim3.model.RestaurantImage;
import com.nbp.tim3.repository.RestaurantImageRepository;
import com.nbp.tim3.repository.RestaurantRepository;
import com.nbp.tim3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantImageService {

    @Autowired
    RestaurantImageRepository restaurantImageRepository;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    public RestaurantImageResponse uploadRestaurantImage(String imageURL, String username) {
        Integer restaurantId = restaurantService.getRestaurantIdByManager(username);
        restaurantImageRepository.addImage(imageURL, restaurantId);
        return new RestaurantImageResponse(imageURL, restaurantId);
    }

    public RestaurantImageResponse uploadRestaurantLogo(String imageURL, String username) {
        Integer restaurantId = restaurantService.getRestaurantIdByManager(username);
        restaurantImageRepository.addLogo(imageURL, restaurantId);

        return new RestaurantImageResponse(imageURL, restaurantId);
    }

    public List<String> getRestaurantImages(int restaurantId) {
        return restaurantImageRepository.getImagesByRestaurantId(restaurantId);
    }

    public void deleteRestaurantImage(int id) {
        restaurantImageRepository.deleteImage(id);
    }
}
