package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_theme_setting")
public class SysUserThemeSetting extends BaseEntity {

    @TableId
    private Long id;

    private String userCode;

    private String navLayout;

    private String topbarColor;

    private String sidebarColor;

    private Integer sidebarCollapsed;
}
