package com.nbp.tim3.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.service.MenuItemService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "menu-item")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    // @Autowired
    //  private MenuItemRepository menuItemRepository;

    /*
    @GetMapping("/all")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllItems());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getItemById(id));
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete a menu item")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the menu item with provided ID"),
            @ApiResponse(responseCode = "404", description = "Menu item with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteMenuItem(
            @Parameter(description = "Menu Item ID", required = true)
            @PathVariable Long id,
            @RequestHeader("username") String username) {
        return new ResponseEntity<>(menuItemService.deleteMenuItem(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update menu item informations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated menu item information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Menu item with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<MenuItem> updateMenuItem(
            @Parameter(description = "MenuItem ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Menu item information to be updated", required = true)
            @Valid @RequestBody MenuItemDto menuItemDto,
            @RequestHeader("username") String username){

        var menuItem = menuItemService.updateMenuItem(menuItemDto, id);
        return  new ResponseEntity<>(menuItem, HttpStatus.CREATED);
    }

    @Operation(description = "Get a menu item by menu item ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the menu item with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Menu.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Menu item with provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<MenuItem> getMenuItem(
            @Parameter(description = "Menu Item ID", required = true)
            @PathVariable  Long id,
            @RequestHeader("username") String username) {

        var menuItem = menuItemService.getMenuItem(id);
        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }

    @PostMapping("/getlist")
    public ResponseEntity<List<MenuItem>> getMenuItemsByList(@RequestBody List<Long> integerList) {
        // return ResponseEntity.ok(menuItemService.getMenuItemsByList(integerList.getIntegerList()));
        return ResponseEntity.ok(menuItemService.getMenuItemsByList(integerList));
    }

    // If error occured in order-service, remove previously added items
    /*@RabbitListener(queues = "menuItemCreateError")
    public void listen(String menuItemsJson) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        try {
            objectMapper.readValue(menuItemsJson, MenuItem[].class);
            List<MenuItem> menuItemsList = objectMapper.readValue(menuItemsJson, new TypeReference<>() {});
            for(var menuItem : menuItemsList) menuItemService.deleteMenuItem(menuItem.getId());
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    public ResponseEntity<String> deleteMenuItem(@RequestBody Long id) {
        // return ResponseEntity.ok(menuItemService.getMenuItemsByList(integerList.getIntegerList()));
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok(("Menu item with id " + id + "deleted!"));
    }
    */
}
