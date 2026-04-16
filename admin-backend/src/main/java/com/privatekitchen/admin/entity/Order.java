package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("orders")
public class Order extends BaseEntity {

    @TableId
    private Long id;

    private String orderNo;

    private Long userId;

    private String shopName;

    private String pickupType;

    private String contactName;

    private BigDecimal amount;

    private String status;

    private String statusText;

    private String note;

    private String orderType;

    private String payType;
}
