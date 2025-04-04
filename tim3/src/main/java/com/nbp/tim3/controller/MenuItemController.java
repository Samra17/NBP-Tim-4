package com.nbp.tim3.controller;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.dto.restaurantimage.RestaurantImageResponse;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.service.FirebaseService;
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
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(path = "/api/menu-item")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable int id) {
        return ResponseEntity.ok(menuItemService.getItemById(id));
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete a menu item")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the menu item with provided ID"),
            @ApiResponse(responseCode = "404", description = "Menu item with provided ID not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody ResponseEntity<?> deleteMenuItem(
            @Parameter(description = "Menu Item ID", required = true)
            @PathVariable Integer id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update menu item informations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated menu item information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MenuItem.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Menu item with provided ID not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
            content = @Content)}
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<MenuItem> updateMenuItem(
            @Parameter(description = "MenuItem ID", required = true)
            @PathVariable int id,
            @Parameter(description = "Menu item information to be updated", required = true)
            @Valid @RequestBody MenuItemDto menuItemDto){

        var menuItem = menuItemService.updateMenuItem(menuItemDto, id);
        return  new ResponseEntity<>(menuItem, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Upload image for menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added menu item image",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantImageResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PostMapping(path="/image/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<String> addMenuItemImage (
            @Parameter(description = "Image file", required = true)
            @Valid @RequestParam("file") MultipartFile file)
    {
        return new ResponseEntity<> (firebaseService.upload(file), HttpStatus.CREATED);
    }


}
