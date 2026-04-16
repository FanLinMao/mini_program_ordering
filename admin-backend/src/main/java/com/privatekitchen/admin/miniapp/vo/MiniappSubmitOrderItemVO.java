package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MiniappSubmitOrderItemVO {

    private String id;

    private String name;

    private Integer count;

    private BigDecimal price;
}
