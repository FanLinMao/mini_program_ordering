package com.privatekitchen.admin.vo;

import lombok.Data;

@Data
public class SysLoginUserVO {

    private Long id;

    private String username;

    private String displayName;

    private String avatar;

    private String roleName;

    private Integer status;

    private String token;
}
