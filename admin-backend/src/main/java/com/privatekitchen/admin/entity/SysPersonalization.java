package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_personalization")
public class SysPersonalization extends BaseEntity {

    @TableId
    private Long id;

    private String logoUrl;

    private String systemName;

    private String systemIntro;

    private String welcomeText;

    private String systemBackgroundImage;

    private String loginBackgroundImage;

    private String themeColor;
}
