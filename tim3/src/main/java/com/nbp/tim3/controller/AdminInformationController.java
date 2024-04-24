package com.nbp.tim3.controller;

import com.nbp.tim3.dto.coupon.CouponPaginatedResponse;
import com.nbp.tim3.service.AdminInformationService;
import com.nbp.tim3.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping(path="/api/admin")
public class AdminInformationController {

    @Autowired
    private AdminInformationService adminInformationService;

    @Operation(description = "Get all orders by restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all orders by restaurant",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HashMap.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/adminorders")
    public ResponseEntity<Map<String, Long>> getAdminOrders(){
        return ResponseEntity.ok(adminInformationService.getAdminOrders());
    }

    @Operation(description = "Get overall revenue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found overall revenue",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/adminspending")
    public ResponseEntity<Long> getAdminSpending(){
        return ResponseEntity.ok(adminInformationService.getAdminSpending());
    }

    @Operation(description = "Get revenue per restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found revenue per restaurant",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HashMap.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/adminrestaurantrevenue")
    public ResponseEntity<Map<String, Long>> getAdminRestaurantRevenue(){
        return ResponseEntity.ok(adminInformationService.getAdminRestaurantRevenue());
    }

}
