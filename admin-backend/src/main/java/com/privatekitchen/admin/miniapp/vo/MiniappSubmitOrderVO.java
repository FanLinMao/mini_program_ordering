package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class MiniappSubmitOrderVO {

    private String orderId;

    private String orderNo;

    private String title;

    private String time;

    private BigDecimal amount;

    private String status;

    private String statusText;

    private String note;

    private String shopName;

    private String pickupType;

    private String contactName;

    private String payType;

    private List<MiniappSubmitOrderItemVO> items = new ArrayList<>();
}
