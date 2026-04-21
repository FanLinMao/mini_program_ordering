package com.privatekitchen.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailItemVO {

    private Long id;

    private Long dishId;

    private String dishName;

    private String dishImage;

    private BigDecimal price;

    private Integer count;

    private BigDecimal amount;
}
