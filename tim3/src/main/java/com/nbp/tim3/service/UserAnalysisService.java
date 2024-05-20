package com.nbp.tim3.service;

import com.nbp.tim3.dto.customer.NumberOfOrdersPerRestaurantResponse;
import com.nbp.tim3.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAnalysisService {

    @Autowired
    private UserRepository userRepository;

    private JasperReport jasperReport;
    @PostConstruct
    public void init(){
        try {
            InputStream employeeReportStream
                    = getClass().getResourceAsStream("/user_analysis_report.jrxml");
            jasperReport = JasperCompileManager.compileReport(employeeReportStream);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void getUserReport(HttpServletResponse response){
        try {
            Map<String, Object> parameters = new HashMap<>();
            JRSaver.saveObject(jasperReport, "user_analysis_report.jasper");
            List<NumberOfOrdersPerRestaurantResponse> userAnalysisReport = new ArrayList<>();
            userAnalysisReport.add(new NumberOfOrdersPerRestaurantResponse());
            userAnalysisReport.addAll(userRepository.getNumberOfOrdersPerRestaurant());
            JRBeanCollectionDataSource jrBeanCollectionDataSourceNumOfOrdRes = new JRBeanCollectionDataSource(userAnalysisReport);

            parameters.put("datasetTable1", jrBeanCollectionDataSourceNumOfOrdRes);
            parameters.put("totalUsers", userRepository.getNumberOfRegisteredUsers());
            parameters.put("totalRestaurantManagers", userRepository.getNumberOfRestaurantManagers());
            parameters.put("totalCouriers", userRepository.getNumberOfCouriers());
            parameters.put("totalCustomers", userRepository.getNumberOfCustomers());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, jrBeanCollectionDataSourceNumOfOrdRes);

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
