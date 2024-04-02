package com.nbp.tim3.service;

import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.repository.MenuItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService {
   @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem getItemById(int id) {
        MenuItem menuItem = menuItemRepository.findById(id);
        if(menuItem == null)
            throw new EntityNotFoundException(String.format("Menu item with id %d does not exist!", id));
        return menuItem;
    }

    public String deleteMenuItem(int id) {
        if(!menuItemRepository.deleteMenuItem(id))
            throw new EntityNotFoundException(String.format("Menu item with id %d does not exist!",id));
        return "Menu Item with id " + id + " is successfully deleted!";
    }

    public MenuItem updateMenuItem(MenuItemDto menuItemDto, int id) {
        try {
            int rowsUpdated = menuItemRepository.updateMenuItem(menuItemDto, id);
            if(rowsUpdated == 0) {
                throw new EntityNotFoundException(String.format("Menu item with id %d does not exist!",id));
            }
            return menuItemRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
