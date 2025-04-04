package com.csu.bakery.dto;
import lombok.Data;
import java.util.Date;

@Data
public class OrderSummaryResponse {

    private Long orderId;

    private Date orderDate;

    private Double grandTotal;
}

