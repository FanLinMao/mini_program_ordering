package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MiniappDishVO {

    private String id;

    private String categoryId;

    private String name;

    private BigDecimal price;

    private String image;

    private Integer monthlySales;

    private Integer praise;

    private String extra;
}
