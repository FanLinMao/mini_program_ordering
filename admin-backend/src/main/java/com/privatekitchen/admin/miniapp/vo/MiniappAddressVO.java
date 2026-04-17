package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

@Data
public class MiniappAddressVO {

    private String id;

    private String contactName;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detailAddress;

    private String fullAddress;

    private String tag;

    private Boolean isDefault;
}
