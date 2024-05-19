package com.nbp.tim3.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderReportResponse {

    private int restaurantId;
    private String restaurantName;
    private int january;
    private int february;
    private int march;
    private int april;
    private int may;
    private int june;
    private int july;
    private int august;
    private int september;
    private int october;
    private int november;
    private int december;
    private int totalCount;
    private float orderIncrease;
    private float januaryRev;
    private float februaryRev;
    private float marchRev;
    private float aprilRev;
    private float mayRev;
    private float juneRev;
    private float julyRev;
    private float augustRev;
    private float septemberRev;
    private float octoberRev;
    private float novemberRev;
    private float decemberRev;
    private float totalRev;
    private float revIncrease;

}
