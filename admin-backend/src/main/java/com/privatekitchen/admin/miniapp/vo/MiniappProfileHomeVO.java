package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

@Data
public class MiniappProfileHomeVO {

    private Integer points;

    private Integer couponCount;

    private String savingsAmount;

    private String memberLevel;

    private String addressSummary;

    private String couponSummary;

    private String serviceSummary;

    private String settingsSummary;

    private String serviceTitle;

    private String serviceHours;

    private String serviceNotice;
}
