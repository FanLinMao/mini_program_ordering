package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_items")
public class OrderItem extends BaseEntity {

    @TableId
    private Long id;

    private Long orderId;

    private Long dishId;

    private String dishName;

    private BigDecimal price;

    private Integer count;

    private BigDecimal amount;
}
