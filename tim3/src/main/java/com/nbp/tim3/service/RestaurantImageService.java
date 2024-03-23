package com.nbp.tim3.service;

import com.nbp.tim3.dto.restaurantimage.RestaurantImageResponse;
import com.nbp.tim3.dto.restaurantimage.RestaurantImageUploadRequest;
import com.nbp.tim3.model.RestaurantImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantImageService {

    //@Autowired
    //RestaurantImageRepository restaurantImageRepository;
    //@Autowired
    //RestaurantRepository restaurantRepository;


    public Long uploadRestaurantImage(RestaurantImageUploadRequest imageData, String userUUID, Long restaurantId) {

        /*var restaurantImage = new RestaurantImage();
        restaurantImage.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow());
        restaurantImage.setCreated(LocalDateTime.now());
        restaurantImage.setCreatedBy(userUUID);
        restaurantImage.setImage(imageData.getImageData());


        var image = restaurantImageRepository.save(restaurantImage);
        return image.getId();*/

        return 1L;
    }

    public List<RestaurantImageResponse> getRestaurantImages(Long restaurantId) {
        /*return restaurantImageRepository.findAllByRestaurantId(restaurantId).stream()
                .map(ri -> new RestaurantImageResponse(ri.getImage(),ri.getId()))
                .collect(Collectors.toList());*/

        return new ArrayList<>();

    }

    public String deleteRestaurantImage(Long id) {
        /*var image = restaurantImageRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Image with id " + id + " does not exist!"));
        restaurantImageRepository.delete(image);
        return "Image with id " + id + " successfully deleted!";*/

        return "Something";
    }
}
