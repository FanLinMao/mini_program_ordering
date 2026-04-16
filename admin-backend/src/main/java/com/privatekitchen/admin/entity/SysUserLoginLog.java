package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_login_log")
public class SysUserLoginLog extends BaseEntity {

    @TableId
    private Long id;

    private String username;

    private String displayName;

    private String actionType;

    private Integer successFlag;

    private String ipAddress;

    private String userAgent;

    private String remark;
}
