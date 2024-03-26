package com.nbp.tim3.controller;

import com.nbp.tim3.dto.category.CategoryCreateRequest;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //@PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(description = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Category> addNewCategory (
            @Parameter(description = "Category name", required = true)
            @Valid @RequestBody CategoryCreateRequest request) {

        var category = categoryService.addNewCategory(request);

        return new ResponseEntity<>(category,HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(description = "Update category name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated category name",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Category> updateCategory (
            @Parameter(description = "Category ID", required = true)
            @PathVariable int id,
            @Parameter(description = "Category name", required = true)
            @RequestBody @Valid CategoryCreateRequest request) {

        Category category = null;
        category = categoryService.updateCategory(request,id);

        return new ResponseEntity<>(category,HttpStatus.OK);
    }


    @Operation(description = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all categories in the system",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) })}
    )
    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<Category>> getAllCategories() {
        var categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }


    @Operation(description = "Get a category by ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the category with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Category with provided ID not found",
                    content = @Content)})
    @GetMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Category> getCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable  int id) {
        var category = categoryService.getCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }



    //@PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(description = "Delete a category")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the category with provided ID"),
            @ApiResponse(responseCode = "404", description = "Category with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<?> deleteCategory(
            @Parameter(description = "Category ID", required = true)
            @PathVariable int id) {

        categoryService.deleteCategory(id);
        return  ResponseEntity.noContent().build();

    }
}
