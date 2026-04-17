package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_coupons")
public class UserCoupon extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Long couponId;

    private String status;

    private LocalDateTime usedAt;

    private LocalDateTime expireAt;
}
