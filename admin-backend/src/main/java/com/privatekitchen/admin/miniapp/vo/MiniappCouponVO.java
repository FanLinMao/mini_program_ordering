package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MiniappCouponVO {

    private String id;

    private String name;

    private String type;

    private BigDecimal discountValue;

    private BigDecimal threshold;

    private String status;

    private String statusText;

    private String expireAt;
}
