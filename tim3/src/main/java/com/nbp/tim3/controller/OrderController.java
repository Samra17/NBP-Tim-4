package com.nbp.tim3.controller;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderPaginatedResponse;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Create an order with possibility of using a coupon")
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
        var order = orderService.addNewOrder(orderCreateRequest);
        return ResponseEntity.ok(order);
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
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','CUSTOMER')")
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
    @GetMapping(path = "/get/customer")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<OrderPaginatedResponse> GetAllCustomerOrders(
            @Parameter(description = "Page number", required = true)
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Number of records per page", required = true)
            @RequestParam(value = "perPage", defaultValue = "10") Integer perPage,
            @Parameter(description = "User username", required = false)
            @RequestHeader(value = "username", required = false) String username) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(username, page, perPage));
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
    @GetMapping("/get/delivery-person")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderPaginatedResponse> getOrdersByDeliveryPersonId(
            @Parameter(description = "Page number", required = true)
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestParam(value = "perPage", defaultValue = "10") Integer perPage,
            @Parameter(description = "User username", required = false)
            @RequestHeader(value = "username", required = false) String username) {
        return ResponseEntity.ok(orderService.getOrdersByCourier(username, page, perPage));
    }

    @Operation(description = "Get all ready for delivery orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched orders",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderPaginatedResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Courier not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @GetMapping("/get/ready-for-delivery")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderPaginatedResponse> getReadyForDeliveryOrders(
            @Parameter(description = "Page number", required = true)
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestParam(value = "perPage", defaultValue = "10") Integer perPage) {
        return ResponseEntity.ok(orderService.getReadyForDeliveryOrders(page, perPage));
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
    @PutMapping("/status/{orderId}/{status}")
    public ResponseEntity<?> changeOrderStatus(
            @Parameter(description = "Order ID", required = true)
            @PathVariable("orderId") Integer orderId,
            @Parameter(description = "New order status", required = true)
            @PathVariable("status") String status) {
        orderService.changeOrderStatus(orderId, Status.fromString(status));
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
    @PutMapping("{orderId}/delivery-person/add")
    public ResponseEntity<OrderResponse> addDeliveryPersonToOrder(
            @Parameter(description = "Order ID", required = true)
            @PathVariable("orderId") Integer orderId,
            @Parameter(description = "User username", required = false)
            @RequestHeader(value = "username", required = false) String username) {

        orderService.addDeliveryPerson(orderId,username);
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
    @GetMapping("/get/restaurant/{status}")
    public ResponseEntity<OrderPaginatedResponse> getOrdersByStatusForRestaurant(
            @Parameter(description = "Page number", required = true)
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestParam(value = "perPage", defaultValue = "10") Integer perPage,
            @Parameter(description = "Order status", required = false)
            @PathVariable(value = "status", required = false) String status,
            @Parameter(description = "User username", required = false)
            @RequestHeader(value = "username", required = false) String username
            ) {

        return ResponseEntity.ok(orderService.getByRestaurantIdAndStatusPage(username, Status.fromString(status), page, perPage));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("annual-report")
    public void getAnnualReportForCurrentYear(HttpServletResponse response) {
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=order_annual_report_pdf " + System.currentTimeMillis() + ".pdf";
        response.setHeader(headerKey,headerValue);

        orderService.getAnnualOrderReport(response, Year.now().getValue());
    }

}
