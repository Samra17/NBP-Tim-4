package com.nbp.tim3.service;

import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.repository.MenuItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService {
   @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllItems() {
        // return menuItemRepository.findAll();
        return new ArrayList<>();
    }

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

    public MenuItem updateMenuItem(MenuItemDto menuItemDto, Long id) {
        /*var exception = new EntityNotFoundException("Menu Item with id " + id + " does not exist!");
        var menuItem = menuItemRepository.findById(id).orElseThrow(() -> exception);
        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setDiscount_price(menuItemDto.getDiscount_price());
        menuItem.setPrep_time(menuItemDto.getPrep_time());
        menuItem.setDate_modified(LocalDateTime.now());
        menuItem.setImage(menuItemDto.getImage());
        menuItemRepository.save(menuItem);
        return menuItem;*/

        return new MenuItem();
    }

    public MenuItem getMenuItem(Long id) {
        /*var exception = new EntityNotFoundException("Menu Item with id " + id + " does not exist!");
        var menuItem = menuItemRepository.findById(id);
        return menuItem.orElseThrow(() -> exception);*/

        return new MenuItem();
    }

    public List<MenuItem> getMenuItemsByList(List<Long> integerList) {
        // return menuItemRepository.findAllById(integerList);
        return new ArrayList<>();
    }

}
