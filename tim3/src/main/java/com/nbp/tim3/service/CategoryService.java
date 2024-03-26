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
        // return StreamSupport.stream(categoryRepository.findAll().spliterator(),false).collect(Collectors.toList());
        return new ArrayList<>();
    }

    public Category getCategory(Long id) {
        /*var exception = new EntityNotFoundException("Category with id " + id + " does not exist!");
        var category = categoryRepository.findById(id);
        return category.orElseThrow(()-> exception);*/

        return new Category();
    }

    public String deleteCategory(Long id) {
        /*var category = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id " + id + " does not exist!"));
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        var restaurantsWithCategory = categoryRepository.getRestaurantsWithCategories(idList);
        restaurantsWithCategory.forEach( r ->
        {
            r.setCategories(new HashSet<>(r.getCategories().stream().filter(c -> c.getId()!=id).collect(Collectors.toList())));
            restaurantRepository.save(r);
        });
        categoryRepository.delete(category);*/

        return "Something";
    }
}
