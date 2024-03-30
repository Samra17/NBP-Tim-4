package com.nbp.tim3.dto.order;

import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.model.OrderMenuItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class OrderResponse {

    private Order order;
    private List<OrderMenuItemResponse> items;


    @Getter
    @Setter
    public static class OrderMenuItemResponse {
        private int id;
        private int quantity;
        private MenuItem menuItem;

        public OrderMenuItemResponse(OrderMenuItem orderMenuItem){
            this.id = orderMenuItem.getId();
            this.quantity = orderMenuItem.getQuantity();
            this.menuItem = orderMenuItem.getMenuItem();
        }

    }

}

