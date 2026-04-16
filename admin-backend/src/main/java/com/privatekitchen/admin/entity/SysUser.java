package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId
    private Long id;

    private String username;

    private String password;

    private String displayName;

    private String phone;

    private String avatar;

    private String roleName;

    private Integer status;

    private String remark;
}
