package com.nbp.tim3.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest implements Serializable {


    @NotNull(message = "Category name must be specified!")
    @Size(min=3,max=80,message = "Category name must be between 3 and 80 characters!")
    private String name;

}
