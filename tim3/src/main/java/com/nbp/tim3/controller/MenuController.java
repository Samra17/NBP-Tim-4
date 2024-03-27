package com.nbp.tim3.controller;

import com.nbp.tim3.dto.menu.MenuDto;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.service.MenuService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = "menu")
public class MenuController {

    @Autowired
    public RestTemplate restTemplate;
    @Autowired
    private MenuService menuService;
/*
    @Operation(description = "Get all menus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all menus",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class))})}
    )
    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<List<Menu>> getAllMenus(
            @RequestHeader("username") String username
    ) {
        var menus = menuService.getAllMenus();

        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @Operation(description = "Get a menu by menu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the menu with provided ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Menu> getMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable Long id,
            @RequestHeader("username") String username) {
        var menu = menuService.getMenu(id);

        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Create a new menu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new menu",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Menu> addNewMenu(
            @Parameter(description = "Information required for menu creation", required = true)
            @Valid @RequestBody MenuDto menuDto,
            @RequestHeader("username") String username) {
        var menu = menuService.addNewMenu(menuDto);

        return new ResponseEntity<>(menu, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update menu informations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated menu information",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<Menu> updateMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Menu information to be updated", required = true)
            @Valid @RequestBody MenuDto menuDto,
            @RequestHeader("username") String username) {

        var menu = menuService.updateMenu(menuDto, id);
        return new ResponseEntity<>(menu, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete a menu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the menu with provided ID"),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable Long id,
            @RequestHeader("username") String username) {

        return new ResponseEntity<>(menuService.deleteMenu(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Set menu items for menu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated menu items",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Menu with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/{id}/set-menu-items")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Menu> setMenuItemsForMenu(
            @Parameter(description = "Menu ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Values of menu items", required = true)
            @RequestBody List<@Valid MenuItemDto> menuItemDtos,
            @RequestHeader("username") String username) {

        var menu = menuService.addMenuItemsToMenu(id, menuItemDtos);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @PostMapping(path = "/add-menu/{restaurantid}")
    public @ResponseBody ResponseEntity<Menu> addNewMenuForRestaurant(
            @PathVariable Long restaurantid,
            @Parameter(description = "Information required for menu creation", required = true)
            @Valid @RequestBody MenuDto menuDto,
            @RequestHeader("username") String username) {
        String restaurantUUID = restTemplate.getForObject("http://restaurant-service/restaurant/uuid/" + restaurantid, String.class);
        menuDto.setRestaurant_uuid(restaurantUUID);
        var menu = menuService.addNewMenu(menuDto);

        return new ResponseEntity<>(menu, HttpStatus.CREATED);
    }



    @GetMapping(path = "/restaurant-menus/uuid/{restaurantUUID}")
    public  List<MenuDto> getRestaurantMenus (@PathVariable String restaurantUUID,
                                           @RequestHeader("username") String username) {

        var menus = menuService.getRestaurantMenusShort(restaurantUUID);
        return menus;
    }

    @GetMapping(path = "/restaurant-menus/active/{restaurantUUID}")
    public  List<Menu> getActiveRestaurantMenus (@PathVariable
                                                 String restaurantUUID,
                                           @RequestHeader("username") String username) {

        var menus = menuService.getActiveRestaurantMenus(restaurantUUID);
        return menus;
    }
*/}
