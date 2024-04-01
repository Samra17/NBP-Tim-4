package com.nbp.tim3.dto.order;

import com.nbp.tim3.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateDto {

    private Integer courierId;
    private Status orderStatus;

}
