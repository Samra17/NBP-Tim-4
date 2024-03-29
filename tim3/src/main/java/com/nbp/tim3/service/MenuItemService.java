package com.nbp.tim3.service;

import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService {
   // @Autowired
    //private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllItems() {
        // return menuItemRepository.findAll();
        return new ArrayList<>();
    }

    public MenuItem getItemById(Long id) {
        return new MenuItem();
    }

    public String deleteMenuItem(Long id) {
        /*var menu = menuItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Menu with id " + id + " does not exist!"));
        menuItemRepository.deleteById(id);
        return "Menu Item with id " + id + " is successfully deleted!";*/

        return "Something";
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
