package com.nbp.tim3.controller;
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
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/menu-item")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable int id) {
        return ResponseEntity.ok(menuItemService.getItemById(id));
    }

   //@PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete a menu item")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the menu item with provided ID"),
            @ApiResponse(responseCode = "404", description = "Menu item with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteMenuItem(
            @Parameter(description = "Menu Item ID", required = true)
            @PathVariable Integer id) {
        return new ResponseEntity<>(menuItemService.deleteMenuItem(id), HttpStatus.OK);
    }

   // @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
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
            @PathVariable int id,
            @Parameter(description = "Menu item information to be updated", required = true)
            @Valid @RequestBody MenuItemDto menuItemDto){

        var menuItem = menuItemService.updateMenuItem(menuItemDto, id);
        return  new ResponseEntity<>(menuItem, HttpStatus.CREATED);
    }




}
