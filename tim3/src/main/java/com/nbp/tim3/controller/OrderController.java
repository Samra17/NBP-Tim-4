package com.nbp.tim3.controller;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderPaginatedResponse;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private final List<Character> chars = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '#', '$', '%', '&', '/', '(', ')', '=', '?', '*');

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Create order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/add")
    public @ResponseBody ResponseEntity<OrderResponse> addNewOrder(
            @Parameter(description = "Order information", required = true)
            @RequestBody OrderCreateRequest orderCreateRequest) {
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
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(description = "Get all customer's orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched customer's orders",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderPaginatedResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @GetMapping(path = "/get/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<OrderPaginatedResponse> GetAllCustomerOrders(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable("customerId") Integer customerId,
            @Parameter(description = "Page number", required = true)
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Number of records per page", required = true)
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId, page, size));
    }

    @Operation(description = "Get all orders by courier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched courier's orders",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderPaginatedResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Courier not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @GetMapping("/get/delivery-person/{courierId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderPaginatedResponse> getOrdersByDeliveryPersonId(
            @Parameter(description = "Courier ID", required = true)
            @PathVariable("courierId") Integer courierId,
            @Parameter(description = "Page number", required = true)
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(orderService.getOrdersByCourierId(courierId, page, size));
    }

    @Operation(description = "Get order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/{id}")
    public @ResponseBody ResponseEntity<OrderResponse> GetOrderById(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @Operation(description = "Get restaurants sorted by number of orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched sorted restaurants",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HashMap.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/count/{sorttype}")
    public @ResponseBody ResponseEntity<Map<String, Long>> GetRestaurantOrderCounts(
            @Parameter(description = "List of restaurant IDs", required = true)
            @RequestBody List<String> restaurantIds,
            @Parameter(description = "Sort direction indicator",required = true)
            @PathVariable("sorttype") String sortType) {
        return ResponseEntity.ok(orderService.getRestaurantOrdersSorted(restaurantIds,sortType));
    }

    @PreAuthorize("hasAnyRole('COURIER','RESTAURANT_MANAGER','CUSTOMER')")
    @Operation(description = "Change order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed order status",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{orderId}/status")
    public ResponseEntity<?> changeOrderStatus(
            @Parameter(description = "Order ID", required = true)
            @PathVariable("orderId") Integer orderId,
            @Parameter(description = "New order status", required = true)
            @RequestHeader("status") Status status) {
        orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @PreAuthorize("hasRole('COURIER')")
    @Operation(description = "Assign order to courier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully assigned order to courier",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{orderId}/delivery-person/add/{courierId}")
    public ResponseEntity<OrderResponse> addDeliveryPersonToOrder(
            @Parameter(description = "Order ID", required = true)
            @PathVariable("orderId") Integer orderId,
            @Parameter(description = "Courier ID", required = true)
            @PathVariable("courierId") Integer courierId) {

        orderService.addDeliveryPerson(orderId, courierId);
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @PreAuthorize("hasAnyRole('COURIER', 'RESTAURANT_MANAGER')")
    @Operation(description = "Get orders by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched orders by status",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderPaginatedResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get/restaurant/{restaurantId}")
    public ResponseEntity<OrderPaginatedResponse> getOrdersByStatusForRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable("restaurantId") Integer restaurantId,
            @Parameter(description = "Page number", required = true)
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestHeader(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "Order status", required = false)
            @RequestHeader(value = "status", required = false) Status status
            ) {
        return ResponseEntity.ok(orderService.getByRestaurantIdAndStatusPage(restaurantId, status, page, size));
    }

}
