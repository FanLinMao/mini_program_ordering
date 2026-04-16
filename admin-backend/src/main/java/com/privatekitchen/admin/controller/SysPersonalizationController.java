package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.SysPersonalization;
import com.privatekitchen.admin.service.SysPersonalizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sys/personalization")
public class SysPersonalizationController {

    private final SysPersonalizationService sysPersonalizationService;

    public SysPersonalizationController(SysPersonalizationService sysPersonalizationService) {
        this.sysPersonalizationService = sysPersonalizationService;
    }

    @GetMapping
    public ApiResponse<SysPersonalization> getConfig() {
        return ApiResponse.success(sysPersonalizationService.getConfig());
    }

    @PutMapping
    public ApiResponse<SysPersonalization> saveConfig(@RequestBody SysPersonalization config) {
        return ApiResponse.success("保存成功", sysPersonalizationService.saveConfig(config));
    }
}
