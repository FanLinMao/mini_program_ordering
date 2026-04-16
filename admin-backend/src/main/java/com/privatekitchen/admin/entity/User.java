package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseEntity {

    @TableId
    private Long id;

    private String openid;

    private String nickname;

    private String phone;

    private String avatar;

    private Integer points;

    private String memberLevel;

    private Integer status;
}
