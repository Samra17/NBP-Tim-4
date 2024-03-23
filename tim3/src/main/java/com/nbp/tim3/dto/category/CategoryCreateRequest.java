package com.nbp.tim3.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class CategoryCreateRequest implements Serializable {


    private String userUUID;
    @NotNull(message = "Category name must be specified!")
    @Size(min=3,max=80,message = "Category name must be between 3 and 80 characters!")
    private String name;

    public CategoryCreateRequest(String name, String userUUID) {
        this.name = name;
        this.userUUID = userUUID;
    }

    public CategoryCreateRequest() {
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
