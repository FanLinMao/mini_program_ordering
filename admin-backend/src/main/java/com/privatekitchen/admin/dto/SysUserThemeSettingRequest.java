package com.privatekitchen.admin.dto;

import lombok.Data;

@Data
public class SysUserThemeSettingRequest {

    private String userCode;

    private String navLayout;

    private String topbarColor;

    private String sidebarColor;

    private Object sidebarCollapsed;
}
