package com.privatekitchen.admin.miniapp.dto;

import lombok.Data;

@Data
public class MiniappAddressSaveRequest {

    private Long id;

    private Long userId;

    private String openid;

    private String contactName;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detailAddress;

    private String tag;

    private Integer isDefault;
}
