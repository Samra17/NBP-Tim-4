package com.nbp.tim3.service;

import com.nbp.tim3.dto.order.*;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.repository.OrderMenuItemRepository;
import com.nbp.tim3.repository.OrderRepository;
import com.nbp.tim3.repository.UserRepository;
import com.nbp.tim3.util.exception.InvalidRequestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMenuItemRepository orderMenuItemRepository;

    @Autowired
    private UserRepository userRepository;

    private JasperReport jasperReport;
    @PostConstruct
    public void init(){
        try {
            InputStream employeeReportStream
                    = getClass().getResourceAsStream("/order_annual_report.jrxml");
            jasperReport = JasperCompileManager.compileReport(employeeReportStream);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public OrderResponse addNewOrder(OrderCreateRequest request) {
        return orderRepository.createOrder(request);
    }

    public OrderPaginatedResponse getOrdersByCustomerId(String username, Integer page, Integer size) {
        var customerId = userRepository.getIdByUsername(username);
        return orderRepository.getByCustomerIdPage(customerId, page, size);
    }

    public OrderPaginatedResponse getOrdersByCourier(String username, Integer page, Integer size) {
        Integer courierId = userRepository.getByUsername(username).getId();
        return orderRepository.getByCourierIdPage(courierId, page, size);
    }

    public OrderPaginatedResponse getByRestaurantIdAndStatusPage(String managerUserananem, Status status, Integer page, Integer size) {
        return orderRepository.getByRestaurantManagerAndStatusPage(managerUserananem, status, page, size);
    }

    public Map<String, Long> getRestaurantOrdersSorted(List<String> restaurantIds, String sortType){
        return orderRepository.getRestaurantOrdersSorted(restaurantIds,sortType);
    }

    public OrderResponse getById(Integer id) {
        OrderResponse orderResponse = orderRepository.getById(id);
        if (orderResponse == null) {
            throw new EntityNotFoundException(String.format("Order with id %d does not exist!", id));
        }

        //List<String> items = orderMenuItemRepository.getOrderMenuItemsByOrder(orderResponse.getId());
        //orderResponse.setItems(items);

        return orderResponse;
    }

    public void addDeliveryPerson(Integer orderId, String username) {
        var user = userRepository.getByUsername(username);
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setCourierId(user.getId());
        orderUpdateDto.setOrderStatus(Status.ACCEPTED_FOR_DELIVERY);
        orderRepository.updateOrder(orderId, orderUpdateDto);
    }

    public void changeOrderStatus(Integer orderId, Status status) {
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        try {
            orderUpdateDto.setOrderStatus(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e.getMessage());
        }
        orderRepository.updateOrder(orderId, orderUpdateDto);
    }

    public OrderPaginatedResponse getReadyForDeliveryOrders(Integer page, Integer perPage) {
        return orderRepository.getReadyForDeliveryOrdersPage(page, perPage);
    }

    public void getAnnualOrderReport(HttpServletResponse response, Integer year){
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("year", year);
            JRSaver.saveObject(jasperReport, "order_annual_report.jasper");
            List<OrderReportResponse> orderAnnualReport = new ArrayList<>();
            orderAnnualReport.add(new OrderReportResponse());
            orderAnnualReport.addAll(orderRepository.getOrderAnnualReport(year));
            JRBeanCollectionDataSource jrBeanCollectionDataSourceFirst = new JRBeanCollectionDataSource(orderAnnualReport);

            List<OrderReportResponse> revenueAnnualReport = orderAnnualReport.subList(1,orderAnnualReport.size());
            JRBeanCollectionDataSource jrBeanCollectionDataSourceRevenue = new JRBeanCollectionDataSource(revenueAnnualReport);

            List<OrderCourierReportResponse> orderCourierAnnualReport = orderRepository.getOrderCourierAnnualReport(year);
            JRBeanCollectionDataSource jrBeanCollectionDataSourceCourier = new JRBeanCollectionDataSource(orderCourierAnnualReport);

            OrderTotalResponse totalOrderAnnualReport = orderRepository.getTotalOrderAnnualReport(2024);
            parameters.put("datasetTable1", jrBeanCollectionDataSourceFirst);
            parameters.put("datasetTable2", jrBeanCollectionDataSourceRevenue);
            parameters.put("datasetTable3", jrBeanCollectionDataSourceCourier);
            parameters.put("totalOrderCount", totalOrderAnnualReport.getTotalCount());
            parameters.put("totalRevenue", totalOrderAnnualReport.getTotalRevenue());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, jrBeanCollectionDataSourceFirst);

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
