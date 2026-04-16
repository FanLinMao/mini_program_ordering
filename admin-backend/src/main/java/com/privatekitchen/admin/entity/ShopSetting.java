package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shop_settings")
public class ShopSetting extends BaseEntity {

    @TableId
    private Long id;

    private String shopName;

    private String address;

    private String notice;

    private String businessHours;

    private Integer takeoutEnabled;

    private Integer cookNowEnabled;

    private Integer wxpayEnabled;
}
