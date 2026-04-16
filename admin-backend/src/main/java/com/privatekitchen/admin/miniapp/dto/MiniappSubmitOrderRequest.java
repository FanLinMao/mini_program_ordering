package com.privatekitchen.admin.miniapp.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MiniappSubmitOrderRequest {

    private Long userId;

    private String openid;

    private String payType;

    private List<MiniappSubmitOrderItemRequest> items = new ArrayList<>();
}
