package com.nbp.tim3.service;

import com.nbp.tim3.dto.category.CategoryCreateRequest;
import com.nbp.tim3.model.Category;
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
    //@Autowired
    //private CategoryRepository categoryRepository;

    //@Autowired
    //private RestaurantRepository restaurantRepository;

    public Category addNewCategory(CategoryCreateRequest request) {
        /*
        Category category = new Category();
        category.setName(request.getName());
        category.setCreated(LocalDateTime.now());
        category.setCreatedBy(request.getUserUUID());
        categoryRepository.save(category);

        return category;
        */

        return new Category();
    }

    public Category updateCategory(CategoryCreateRequest request, Long id) {
        /*
        var exception = new EntityNotFoundException("Category with id " + id + " does not exist!");
        var category = categoryRepository.findById(id).orElseThrow(()-> exception);
        category.setName(request.getName());
        category.setModified(LocalDateTime.now());
        category.setModifiedBy(request.getUserUUID());
        categoryRepository.save(category);
        return category;
        */

        return new Category();
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
