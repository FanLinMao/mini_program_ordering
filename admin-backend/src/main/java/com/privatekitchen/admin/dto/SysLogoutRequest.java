package com.privatekitchen.admin.dto;

import lombok.Data;

@Data
public class SysLogoutRequest {

    private String username;

    private String token;
}
