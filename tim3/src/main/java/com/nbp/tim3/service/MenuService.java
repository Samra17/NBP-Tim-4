package com.nbp.tim3.service;

import com.nbp.tim3.dto.menu.MenuCreateRequest;
import com.nbp.tim3.dto.menu.MenuDto;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.dto.menu.MenuUpdateDto;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.repository.MenuRepository;
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

    //@Autowired
    //private MenuItemRepository menuItemRepository;

    //@Autowired
    //private RabbitTemplate rabbitTemplate;

    public List<Menu> getAllMenus() {
        // return StreamSupport.stream(menuRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return new ArrayList<>();
    }

    public Menu getMenu(int id) {
        var menu = menuRepository.findById(id);
        if(menu == null)
            throw new EntityNotFoundException(String.format("Menu with id %d does not exist!",id));
        return menu;
    }

    public MenuDto addNewMenu(MenuCreateRequest menuDto) {
        MenuDto menu = new MenuDto();
        menu.setName(menuDto.getName());
        menu.setActive(menuDto.isActive());
        menu.setRestaurantID(menuDto.getRestaurantID());
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

    public Menu addMenuItemsToMenu(Long id, List<MenuItemDto> menuItemsDao) {
        /*var exception = new EntityNotFoundException("Menu with id " + id + " does not exist!");
        var menu = menuRepository.findById(id).orElseThrow(()->exception);

        if(menu.getMenuItems() == null)
            menu.setMenuItems(new ArrayList<>());

        var items = menu.getMenuItems();
        var newItems = new ArrayList<MenuItem>();

        for (var menuItemDao: menuItemsDao) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName(menuItemDao.getName());
            menuItem.setDescription(menuItemDao.getDescription());
            menuItem.setPrice(menuItemDao.getPrice());
            menuItem.setPrep_time(menuItemDao.getPrep_time());
            menuItem.setDiscount_price(menuItemDao.getDiscount_price());
            //menuItem.setUuid(UUIDGenerator.generateType1UUID().toString());
            menuItem.setDate_created(LocalDateTime.now());
            menuItem.setImage(menuItemDao.getImage());
            items.add(menuItem);
            newItems.add(menuItem);
        }
        menu.setMenuItems(items);
        menu.setDate_modified(LocalDateTime.now());
        menuRepository.save(menu);

        try {
            List<Long> idList = new ArrayList<>();
            for(var item : newItems) idList.add(item.getId());

            var newItemsWithUUID = menuItemRepository.findAllById(idList);
            var objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new ParameterNamesModule());
        } catch (Exception e) {
            System.out.println("Something went wrong when fetching items");
        }
        return menu;*/

        return new Menu();
    }

    public List<MenuDto> getRestaurantMenus(int restaurantID) {
        return menuRepository.getMenusForRestaurant(restaurantID);
    }


    public List<Menu> getActiveRestaurantMenus(int restaurantID) {
         return menuRepository.getActiveMenusForRestaurant(restaurantID);
    }
}
