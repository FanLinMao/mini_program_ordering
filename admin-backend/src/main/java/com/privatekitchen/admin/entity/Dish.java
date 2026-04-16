package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dishes")
public class Dish extends BaseEntity {

    @TableId
    private Long id;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private String image;

    private Integer monthlySales;

    private Integer praise;

    private String extra;

    private Integer sort;

    private Integer status;
}
