package com.nbp.tim3.service;


import com.nbp.tim3.repository.AdminInformationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminInformationService {

    @Autowired
    AdminInformationRepository adminInformationRepository;

    public Map<String, Long> getAdminOrders() {
        return adminInformationRepository.getOrdersByRestaurant();
    }

    public Long getAdminSpending(){
        return adminInformationRepository.getTotalPriceOfOrders();
    }

    public Map<String, Long> getAdminRestaurantRevenue() {
        return adminInformationRepository.getRevenueByRestaurant();
    }


}
