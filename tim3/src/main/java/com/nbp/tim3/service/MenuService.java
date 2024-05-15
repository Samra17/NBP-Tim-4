package com.nbp.tim3.service;

import com.nbp.tim3.dto.menu.MenuCreateRequest;
import com.nbp.tim3.dto.menu.MenuDto;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.dto.menu.MenuUpdateDto;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.repository.MenuRepository;
import com.nbp.tim3.repository.RestaurantRepository;
import com.nbp.tim3.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.MemoryNotificationInfo;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Menu getMenu(int id) {
        var menu = menuRepository.findById(id);
        if(menu == null)
            throw new EntityNotFoundException(String.format("Menu with id %d does not exist!",id));
        return menu;
    }

    public MenuDto addNewMenu(MenuCreateRequest menuDto, String username) {
        Integer restaurantId = restaurantRepository.getRestaurantIdByManagerUsername(username);
        MenuDto menu = new MenuDto();
        menu.setName(menuDto.getName());
        menu.setActive(menuDto.isActive());
        menu.setRestaurantID(restaurantId);
        menuRepository.addMenu(menu);
        return menu;

    }
    public String deleteMenu(int id) {
        if(!menuRepository.deleteMenu(id))
            throw new EntityNotFoundException(String.format("Menu with id %d does not exist!",id));
        return "Menu with id " + id + " is successfully deleted!";

    }

    public Menu updateMenu(MenuUpdateDto menuDto, int id) {
        Integer restaurantId = menuRepository.findMenuRestaurant(id);
        if(restaurantId == null)
            throw new EntityNotFoundException(String.format("Menu with id %d does not exist!",id));

        MenuCreateRequest menu = new MenuCreateRequest( menuDto.isActive(), restaurantId, menuDto.getName());
        try {
            int rowsUpdated = menuRepository.updateMenu(menu, id);
            if(rowsUpdated == 0) {
                throw new EntityNotFoundException(String.format("Menu with id %d does not exist!",id));
            }

            return menuRepository.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMenuItemsToMenu(int id, List<MenuItemDto> menuItemsDao) {
       menuRepository.addMenuItemsToMenu(id, menuItemsDao);
    }

    public List<MenuDto> getRestaurantMenus(String username) {
        return menuRepository.getMenusForRestaurant(restaurantRepository.getRestaurantIdByManagerUsername(username));
    }


    public List<Menu> getActiveRestaurantMenus(int restaurantID) {
         return menuRepository.getActiveMenusForRestaurant(restaurantID);
    }
}
