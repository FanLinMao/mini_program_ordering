package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    @TableId
    private Long id;

    private Long parentId;

    private String name;

    private String title;

    private String path;

    private String component;

    private String icon;

    private String type;

    private Integer status;

    private Integer sort;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
