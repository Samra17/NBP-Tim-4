package com.nbp.tim3.service;

import com.nbp.tim3.dto.category.CategoryCreateRequest;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    //@Autowired
    //private RestaurantRepository restaurantRepository;

    public Category addNewCategory(CategoryCreateRequest request) {

        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.addCategory(category);

        return category;

    }

    public Category updateCategory(CategoryCreateRequest request, int id) {

        Category category = new Category(id, request.getName());
        int rowsUpdated = categoryRepository.updateCategory(category);

        if(rowsUpdated == 0) {
            throw new EntityNotFoundException(String.format("Category with id %d does not exist!",id));
        }

        return category;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    public Category getCategory(int id) {
        var category = categoryRepository.getById(id);

        if(category == null) {
            throw  new EntityNotFoundException(String.format("Category with id %d does not exist!",id));
        } else {
            return category;
        }

    }


    public void deleteCategory(int id) {

        if(!categoryRepository.deleteCategory(id))
            throw new EntityNotFoundException(String.format("Category with id %d does not exist!",id));

    }

}
