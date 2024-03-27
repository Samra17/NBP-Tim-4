package com.nbp.tim3.dto.category;

import com.nbp.tim3.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private int id;
    private String name;


    public CategoryResponse(Category category) {
        id = category.getId();
        name = category.getName();
    }

}

