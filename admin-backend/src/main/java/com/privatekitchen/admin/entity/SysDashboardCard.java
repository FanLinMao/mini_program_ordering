package com.privatekitchen.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dashboard_card")
public class SysDashboardCard extends BaseEntity {

    @TableId
    private Long id;

    private String cardKey;

    private String title;

    private String cardValue;

    private String extraText;

    private Integer enabled;

    private Integer sort;
}
