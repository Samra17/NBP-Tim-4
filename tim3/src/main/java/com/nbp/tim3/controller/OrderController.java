package com.nbp.tim3.controller;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private final List<Character> chars = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '#', '$', '%', '&', '/', '(', ')', '=', '?', '*');

    //    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Create order")
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<OrderResponse> addNewOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        int orderId = orderService.addNewOrder(orderCreateRequest);
        return ResponseEntity.ok(orderService.getById(orderId));
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
    @Operation(description = "Get all customer's orders")
    @GetMapping(path = "/get/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<OrderResponse>> GetAllCustomerOrders(
            @PathVariable("customerId") Integer customerId,
            @RequestHeader(value = "page", defaultValue = "0") Integer page,
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId, page, size));
    }

    @Operation(description = "Get all orders by delivery person")
    @GetMapping("/get/delivery-person/{courierId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<OrderResponse>> getOrdersByDeliveryPersonId(
            @PathVariable("courierId") Integer courierId,
            @RequestHeader(value = "page", defaultValue = "0") Integer page,
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(orderService.getOrdersByCourierId(courierId, page, size));
    }

    @Operation(description = "Get order by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/{id}")
    public @ResponseBody ResponseEntity<OrderResponse> GetOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getById(id));
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
//    @PreAuthorize("hasAnyRole('COURIER','RESTAURANT_MANAGER','CUSTOMER')")
    @PutMapping("{orderId}/status")
    public ResponseEntity<?> changeOrderStatus(
            @PathVariable("orderId") Integer orderId,
            @RequestHeader("status") Status status) {
        orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok(orderService.getById(orderId));
    }
//
//    @PreAuthorize("hasRole('COURIER')")
    @PutMapping("{orderId}/delivery-person/add/{courierId}")
    public ResponseEntity<OrderResponse> addDeliveryPersonToOrder(
            @PathVariable("orderId") Integer orderId,
            @PathVariable("courierId") Integer courierId) {

        orderService.addDeliveryPerson(orderId, courierId);
        return ResponseEntity.ok(orderService.getById(orderId));
    }
//
//    @PreAuthorize("hasRole('COURIER')")
//    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @GetMapping("/get/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatusForRestaurant(
            @PathVariable("restaurantId") Integer restaurantId,
            @RequestHeader(value = "page", defaultValue = "0") Integer page,
            @RequestHeader(value = "size", defaultValue = "10") Integer size,
            @RequestHeader(value = "status", required = false) Status status
            ) {
        return ResponseEntity.ok(orderService.getByRestaurantIdAndStatusPage(restaurantId, status, page, size));
    }

}
