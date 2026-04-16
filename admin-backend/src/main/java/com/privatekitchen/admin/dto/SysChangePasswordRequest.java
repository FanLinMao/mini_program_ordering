package com.privatekitchen.admin.dto;

import lombok.Data;

@Data
public class SysChangePasswordRequest {

    private String oldPassword;

    private String newPassword;
}
