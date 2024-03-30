package com.nbp.tim3.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(path="/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private final List<Character> chars = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '#', '$', '%', '&', '/', '(', ')', '=', '?', '*');

//    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<?> addNewOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        orderService.addNewOrder(orderCreateRequest);
        return ResponseEntity.ok().build();
    }

//    @Operation(description = "Update order by id")
//    @ApiResponses(value = {
//            @ApiResponse( responseCode = "200", description = "Successfully patched order",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = Order.class))}),
//            @ApiResponse( responseCode = "304", description = "Invalid patch path", content = @Content),
//            @ApiResponse( responseCode = "400", description = "Invalid patch", content = @Content),
//            @ApiResponse( responseCode = "404", description = "Order not found", content = @Content)
//    })
//    @PatchMapping(path = "/update/{id}", consumes = "application/json")
//    @ResponseBody
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Order> UpdateOrderStatus(@PathVariable Long id, @RequestBody JsonNode patch) throws IOException {
//        return ResponseEntity.ok(new Order());
//    }
//
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
//    @GetMapping(path = "/get")
//    @ResponseStatus(HttpStatus.OK)
//    public @ResponseBody Iterable<Order> GetAllOrders(@RequestHeader("username") String username) {
//        return new ArrayList<Order>();
//    }
//
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/{id}")
    public @ResponseBody ResponseEntity<OrderResponse> GetOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }
//
//    @PostMapping(path = "/count/{sorttype}")
//    public @ResponseBody Map<String, Long> GetRestaurantOrderCounts(@RequestBody List<String> restaurantUids, @PathVariable String sorttype) {
//        return new HashMap<>();
//    }
//
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
//    @GetMapping("/adminorders")
//    public Map<String, Long> getAdminOrders(){
//        return new HashMap<>();
//    }
//
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
//    @GetMapping("/adminspending")
//    public Long getAdminSpending(){
//        return 100L;
//    }
//
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
//    @GetMapping("/adminrestaurantrevenue")
//    public Map<String, Long> getAdminRestaurantRevenue(){
//        return new HashMap<>();
//    }
//
//    @PreAuthorize("hasRole('CUSTOMER')")
//    @GetMapping("/getforuser")
//    public ResponseEntity<List<OrderResponse>> getAllUserOrders(@RequestHeader("uuid") String userUuid) {
//        return ResponseEntity.ok(orderService.getOrdersByUserUUID(userUuid));
//    }
//
//    @PreAuthorize("hasAnyRole('COURIER','RESTAURANT_MANAGER','CUSTOMER')")
//    @PutMapping("/status/{id}/{status}")
//    public ResponseEntity<?> changeOrderStatus(@PathVariable Long id ,@PathVariable String status) throws JsonProcessingException {
//        return ResponseEntity.ok(new OrderResponse(new Order()));
//    }
//
//    @PreAuthorize("hasRole('COURIER')")
//    @PutMapping("/adddeliveryperson/{id}")
//    public ResponseEntity<OrderResponse> addDeliveryPersonToOrder(@PathVariable Long id, @RequestHeader("uuid") String uuid, @RequestHeader("username") String username) {
//        return ResponseEntity.ok(new OrderResponse(new Order()));
//    }
//
//    @PreAuthorize("hasRole('COURIER')")
//    @GetMapping("/get/deliveryperson")
//    public ResponseEntity<List<OrderResponse>> getOrdersByDeliveryPersonId(@RequestHeader("uuid") String uuid) {
//        return ResponseEntity.ok(new ArrayList<>());
//    }
//
//    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
//    @GetMapping("/get/restaurant/{uuid}/pending")
//    public ResponseEntity<List<OrderResponse>> getPendingOrdersForRestaurant(@PathVariable("uuid") String uuid) {
//        return ResponseEntity.ok(orderService.getPendingOrdersForRestaurant(uuid));
//    }
//
//    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
//    @GetMapping("/get/restaurant/{uuid}/in-preparation")
//    public ResponseEntity<List<OrderResponse>> getInPreparationOrdersForRestaurant(@PathVariable("uuid") String uuid) {
//        return ResponseEntity.ok(orderService.getInPreparationOrdersForRestaurant(uuid));
//    }
//
//    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
//    @GetMapping("/get/restaurant/{uuid}/ready-for-delivery")
//    public ResponseEntity<List<OrderResponse>> getReadyForDeliveryOrdersForRestaurant(@PathVariable("uuid") String uuid) {
//        return ResponseEntity.ok(orderService.getReadyForDeliveryOrdersForRestaurant(uuid));
//    }
//
//    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
//    @GetMapping("/get/restaurant/{uuid}/delivered")
//    public ResponseEntity<List<OrderResponse>> getDeliveredOrdersForRestaurant(@PathVariable("uuid") String uuid) {
//        return ResponseEntity.ok(orderService.getDeliveredOrdersForRestaurant(uuid));
//    }

}
