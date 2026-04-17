package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_addresses")
public class UserAddress extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private String contactName;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detailAddress;

    private String tag;

    private Integer isDefault;
}
