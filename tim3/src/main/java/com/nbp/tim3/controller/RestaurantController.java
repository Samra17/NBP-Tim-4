package com.nbp.tim3.controller;

import com.nbp.tim3.dto.pagination.PaginatedRequest;
import com.nbp.tim3.dto.pagination.PaginatedResponse;
import com.nbp.tim3.dto.restaurant.*;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.service.FavoriteRestaurantService;
import com.nbp.tim3.service.RestaurantImageService;
import com.nbp.tim3.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private FavoriteRestaurantService favoriteRestaurantService;

    @Autowired
    private RestaurantImageService restaurantImageService;



    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(description = "Create a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new restaurant",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<RestaurantCreateResponse> addNewRestaurant (
            @Parameter(description = "Information required for restaurant creation", required = true)
            @Valid @RequestBody RestaurantCreateRequest request) {

        var restaurant = restaurantService.addNewRestaurant(request);

        return new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update restaurant information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated restaurant information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<RestaurantUpdateResponse> updateRestaurant (
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable int id,
            @Parameter(description = "Restaurant information to be updated", required = true)
            @RequestBody @Valid RestaurantUpdateRequest request) {

        RestaurantUpdateResponse restaurant = null;
        restaurant = restaurantService.updateRestaurant(request,id);

        return new ResponseEntity<>(restaurant,HttpStatus.OK);
    }


    @Operation(description = "Get all restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all restaurants in the system",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) })}
    )
    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<RestaurantPaginatedShortResponse> getAllRestaurants(@RequestParam(name="page") int page,
                                                                                            @RequestParam(name="perPage")int recordsPerPage,
                                                                                            @RequestHeader("username") String username) {

       PaginatedRequest request = new PaginatedRequest(page,recordsPerPage);
        var restaurants = restaurantService.searchForRestaurants(request,username,null,null,false);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping(path="/all/full")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<RestaurantPaginatedResponse> getAllFullRestaurants(
            @RequestParam(name="page") int page,
            @RequestParam(name="perPage") int recordsPerPage
    ) {
        PaginatedRequest paginatedRequest = new PaginatedRequest(page,recordsPerPage);

        var restaurants = restaurantService.getFullRestaurants(paginatedRequest);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }


    @Operation(description = "Search for restaurants based on filter and sorting criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all restaurants fulfilling the provided criteria",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantShortResponse[].class)) })}
    )
    @GetMapping(path="/search")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<RestaurantPaginatedShortResponse> searchForRestaurants(@RequestParam(name="page") int page,
                                                                                            @RequestParam(name="perPage")int recordsPerPage,
                                                                                            @RequestHeader("username") String username,
                                                                                            @RequestParam(required = false)  String name,
                                                                                            @RequestParam(required = false)  List<Integer> categoryIds,
                                                                                            @RequestParam(required = false)  boolean isOfferingDiscount,
                                                                                            @RequestParam(required = false) String sortBy,
                                                                                            @RequestParam(required = false) boolean ascending) {

        PaginatedRequest paginatedRequest = new PaginatedRequest(page,recordsPerPage);
        FilterRestaurantRequest filterRequest = null;
        if(name!=null || isOfferingDiscount || categoryIds!=null)
            filterRequest = new FilterRestaurantRequest(name,categoryIds,isOfferingDiscount);

        var restaurants = restaurantService.searchForRestaurants(paginatedRequest,username,filterRequest,sortBy,ascending);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }


    @Operation(description = "Get a restaurant by restaurant ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the restaurant with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)})
    @GetMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<RestaurantShortResponse> getRestaurantById(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable  int id,
            @RequestHeader("username") String username) {

        var restaurant = restaurantService.getRestaurantById(id,username);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Get a restaurant by restaurant manager id")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the restaurant with provided restaurant manager UUID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided restaurant manager UUID not found",
                    content = @Content)})
    @GetMapping(path="/manager")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<RestaurantResponse> getRestaurantByManager(
            @RequestHeader("username") String managerUsername) {

        var restaurant = restaurantService.getRestaurantByManager(managerUsername);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Get restaurant id by restaurant manager")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the restaurant with provided restaurant manager UUID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided restaurant manager UUID not found",
                    content = @Content)})
    @GetMapping(path="/id/manager")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Integer> getRestaurantIdByManager(
            @RequestHeader("username") String managerUsername) {

        var restaurantId = restaurantService.getRestaurantIdByManager(managerUsername);
        return new ResponseEntity<>(restaurantId, HttpStatus.OK);
    }


    @Operation(description = "Get a full restaurant response by restaurant ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the restaurant with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)})
    @GetMapping(path="/full/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<RestaurantResponse> getRestaurantFullResponseById(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable  int id) {

        var restaurant = restaurantService.getRestaurantFullResponseById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    /*
    @Operation(description = "Get restaurants with categories")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found restaurants with provided categories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    })})
    @GetMapping(path="/category")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<Restaurant>> getRestaurantsWithCategories(
            @Parameter(description = "List of category IDs", required = true)
            @RequestParam List<Long> categoryIds) {

        return new ResponseEntity<>(restaurantService.getRestaurantsWithCategories(categoryIds),HttpStatus.OK);
    }

    @Operation(description = "Get restaurant average rating")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated average restaurant rating",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)
    })
    @GetMapping(path="/{id}/rating")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Double> getAverageRatingForRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id) {

        return new ResponseEntity<>(restaurantService.calculateAverageRatingForRestaurant(id),HttpStatus.OK);
    }

    @Operation(description = "Get user's favorite restaurants")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found user's favorite restaurants",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    })
    })
    @GetMapping(path="/favorites")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<RestaurantShortResponse>> getFavoriteRestaurants(
            @RequestHeader("uuid") String userUUID) {
        return new ResponseEntity<>(favoriteRestaurantService.getFavoriteRestaurants(userUUID),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(description = "Delete a restaurant")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the restaurant with provided ID"),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @RequestHeader("username") String username) {

        return new ResponseEntity<>(restaurantService.deleteRestaurant(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Set restaurant categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated restaurant categories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/{id}/add-categories")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Restaurant> addCategoriesToRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "List of category IDs", required = true)
            @RequestBody List<Long> categoryIds,
            @RequestHeader("uuid") String userUUID,
            @RequestHeader("username") String username) {
        var restaurant = restaurantService.addCategoriesToRestaurant(id,categoryIds,userUUID);

        return  new ResponseEntity<>(restaurant,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Set restaurant opening hours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated restaurant opening hours",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/{id}/set-opening-hours")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Restaurant> setRestaurantOpeningHours(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Values of daily opening and closing hours", required = true)
            @Valid @RequestBody OpeningHoursCreateRequest request,
            @RequestHeader("uuid") String userUUID,
            @RequestHeader("username") String username) {
        var restaurant = restaurantService.setRestaurantOpeningHours(id,request,userUUID);

        return  new ResponseEntity<>(restaurant,HttpStatus.OK);
    }

    /*@PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Add restaurant to user's favorite restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added restaurant to favorites",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteRestaurant.class)) }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PostMapping(path="/{id}/add-to-favorites")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<FavoriteRestaurant> addRestaurantToFavorites(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @RequestHeader("uuid") String user,
            @RequestHeader("username") String username
    ) {

        var favoriteRestaurant = favoriteRestaurantService.addRestaurantToFavorites(id,user);

        return new ResponseEntity<>(favoriteRestaurant,HttpStatus.CREATED);
    }*/

    /*@PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Remove restaurant from user's favorite restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed restaurant from favorites",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteRestaurant.class)) }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/{id}/remove-from-favorites")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> removeRestaurantFromFavorites(
            @Parameter(description = "Restaurant ID",required = true)
            @PathVariable Long id,
            @RequestHeader("uuid") String user,
            @RequestHeader("username") String username) {

        favoriteRestaurantService.removeRestaurantFromFavorites(id,user);

        return new ResponseEntity<>("Successfully removed restaurant with id " + id + " from favorites!",HttpStatus.OK);
    }

    @Operation(description = "Get restaurant UUID by restaurant ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found restaurant UUID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @GetMapping(path="/uuid/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> getRestaurantUUIDByRestaurantId(
            @Parameter(description = "Restaurant ID",required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(restaurantService.getRestaurantUUID(id),HttpStatus.OK);
    }

    @Operation(description = "Get images by Restaurant id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all images",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @GetMapping(path="/image/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<RestaurantImageResponse>> getRestaurantImages(
            @Parameter(description = "Restaurant ID",required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(restaurantImageService.getRestaurantImages(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Upload image to restaurant gallery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added restaurant image",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/image/add/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Long> addRestaurantImage (
            @Parameter(description = "Image data", required = true)
            @Valid @RequestBody RestaurantImageUploadRequest request,
            @Parameter (description = "Restaurant id", required = true)
            @PathVariable("id") Long restaurantid,
            @RequestHeader("uuid") String uuid,
            @RequestHeader("username") String username) {

        var id = restaurantImageService.uploadRestaurantImage(request, uuid,restaurantid);

        return new ResponseEntity<>(id,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete an image from restaurant gallery")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the image with provided ID"),
            @ApiResponse(responseCode = "404", description = "Image with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/image/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteRestaurantImage(
            @Parameter(description = "Image ID", required = true)
            @PathVariable Long id,
            @RequestHeader("username") String username) {

        return new ResponseEntity<>(restaurantImageService.deleteRestaurantImage(id),HttpStatus.OK);
    }

    @Operation(description = "Get number of customers who marked the restaurant as favorite")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched number of customers who marked the restaurant with provided UUID as a favorite"),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided UUID not found",
                    content = @Content)})
    @GetMapping(path="/favorites/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> getCustomersFavorited(@Parameter(description = "Restaurant UUID",required = true)
                                                      @PathVariable("uuid") String restaurantUUID) {

        return ResponseEntity.ok(restaurantService.getCustomersFavorited(restaurantUUID));
    }
    */
}
