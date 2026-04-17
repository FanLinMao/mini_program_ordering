package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("coupons")
public class Coupon extends BaseEntity {

    @TableId
    private Long id;

    private String name;

    private String type;

    private BigDecimal discountValue;

    private BigDecimal threshold;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;
}
