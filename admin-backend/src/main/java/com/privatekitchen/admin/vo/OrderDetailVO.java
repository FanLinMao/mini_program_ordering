package com.privatekitchen.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {

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

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private List<OrderDetailItemVO> items;
}
