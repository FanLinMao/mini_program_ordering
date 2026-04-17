package com.privatekitchen.admin.miniapp.dto;

import lombok.Data;

@Data
public class MiniappLogoutRequest {

    private Long userId;

    private String openid;
}
