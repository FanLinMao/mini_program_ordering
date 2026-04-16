package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.dto.SysUserThemeSettingRequest;
import com.privatekitchen.admin.entity.SysUserThemeSetting;
import com.privatekitchen.admin.service.SysUserThemeSettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sys/theme-settings")
public class SysThemeSettingController {

    private final SysUserThemeSettingService sysUserThemeSettingService;

    public SysThemeSettingController(SysUserThemeSettingService sysUserThemeSettingService) {
        this.sysUserThemeSettingService = sysUserThemeSettingService;
    }

    @GetMapping("/current")
    public ApiResponse<SysUserThemeSetting> getCurrentSetting(
            @RequestParam(value = "userCode", required = false, defaultValue = "admin") String userCode) {
        return ApiResponse.success(sysUserThemeSettingService.getCurrentSetting(userCode));
    }

    @PostMapping
    public ApiResponse<SysUserThemeSetting> saveOrUpdate(@RequestBody SysUserThemeSettingRequest request) {
        SysUserThemeSetting setting = new SysUserThemeSetting();
        setting.setUserCode(request.getUserCode());
        setting.setNavLayout(request.getNavLayout());
        setting.setTopbarColor(request.getTopbarColor());
        setting.setSidebarColor(request.getSidebarColor());
        setting.setSidebarCollapsed(parseSidebarCollapsed(request.getSidebarCollapsed()));
        return ApiResponse.success("保存成功", sysUserThemeSettingService.saveOrUpdateSetting(setting));
    }

    private Integer parseSidebarCollapsed(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Boolean boolValue) {
            return boolValue ? 1 : 0;
        }
        if (value instanceof Number number) {
            return number.intValue() == 0 ? 0 : 1;
        }
        String text = String.valueOf(value).trim();
        if ("true".equalsIgnoreCase(text) || "1".equals(text)) {
            return 1;
        }
        return 0;
    }
}
