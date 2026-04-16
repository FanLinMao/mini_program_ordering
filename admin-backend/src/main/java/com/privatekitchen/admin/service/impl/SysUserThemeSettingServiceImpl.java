package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.SysUserThemeSettingDao;
import com.privatekitchen.admin.entity.SysUserThemeSetting;
import com.privatekitchen.admin.service.SysUserThemeSettingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysUserThemeSettingServiceImpl implements SysUserThemeSettingService {

    private static final String DEFAULT_USER_CODE = "admin";
    private static final String DEFAULT_NAV_LAYOUT = "side";
    private static final String DEFAULT_TOPBAR_COLOR = "linear-gradient(90deg, #34c06a 0%, #46b876 100%)";
    private static final String DEFAULT_SIDEBAR_COLOR = "linear-gradient(180deg, #243244 0%, #26364b 100%)";

    private final SysUserThemeSettingDao sysUserThemeSettingDao;

    public SysUserThemeSettingServiceImpl(SysUserThemeSettingDao sysUserThemeSettingDao) {
        this.sysUserThemeSettingDao = sysUserThemeSettingDao;
    }

    @Override
    public SysUserThemeSetting getCurrentSetting(String userCode) {
        String targetUserCode = normalizeUserCode(userCode);
        SysUserThemeSetting setting = sysUserThemeSettingDao.selectOne(new LambdaQueryWrapper<SysUserThemeSetting>()
                .eq(SysUserThemeSetting::getUserCode, targetUserCode)
                .eq(SysUserThemeSetting::getDelFlag, 0)
                .last("limit 1"));
        return setting != null ? setting : buildDefaultSetting(targetUserCode);
    }

    @Override
    public SysUserThemeSetting saveOrUpdateSetting(SysUserThemeSetting setting) {
        String targetUserCode = normalizeUserCode(setting.getUserCode());
        LocalDateTime now = LocalDateTime.now();

        SysUserThemeSetting existing = sysUserThemeSettingDao.selectOne(new LambdaQueryWrapper<SysUserThemeSetting>()
                .eq(SysUserThemeSetting::getUserCode, targetUserCode)
                .eq(SysUserThemeSetting::getDelFlag, 0)
                .last("limit 1"));

        if (existing == null) {
            setting.setUserCode(targetUserCode);
            setting.setNavLayout(normalizeNavLayout(setting.getNavLayout()));
            setting.setTopbarColor(normalizeTopbarColor(setting.getTopbarColor()));
            setting.setSidebarColor(normalizeSidebarColor(setting.getSidebarColor()));
            setting.setSidebarCollapsed(setting.getSidebarCollapsed() == null ? 0 : setting.getSidebarCollapsed());
            setting.setDelFlag(0);
            setting.setCreateTime(now);
            setting.setCreateBy("admin");
            setting.setUpdateTime(now);
            setting.setUpdateBy("admin");
            sysUserThemeSettingDao.insert(setting);
            return setting;
        }

        existing.setNavLayout(normalizeNavLayout(setting.getNavLayout()));
        existing.setTopbarColor(normalizeTopbarColor(setting.getTopbarColor()));
        existing.setSidebarColor(normalizeSidebarColor(setting.getSidebarColor()));
        existing.setSidebarCollapsed(setting.getSidebarCollapsed() == null ? 0 : setting.getSidebarCollapsed());
        existing.setUpdateTime(now);
        existing.setUpdateBy("admin");
        sysUserThemeSettingDao.updateById(existing);
        return existing;
    }

    private SysUserThemeSetting buildDefaultSetting(String userCode) {
        SysUserThemeSetting setting = new SysUserThemeSetting();
        setting.setUserCode(userCode);
        setting.setNavLayout(DEFAULT_NAV_LAYOUT);
        setting.setTopbarColor(DEFAULT_TOPBAR_COLOR);
        setting.setSidebarColor(DEFAULT_SIDEBAR_COLOR);
        setting.setSidebarCollapsed(0);
        setting.setDelFlag(0);
        return setting;
    }

    private String normalizeUserCode(String userCode) {
        return defaultText(userCode, DEFAULT_USER_CODE);
    }

    private String defaultText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private String normalizeNavLayout(String value) {
        String target = defaultText(value, DEFAULT_NAV_LAYOUT);
        if ("classic".equalsIgnoreCase(target)) {
            return "side";
        }
        if ("compact".equalsIgnoreCase(target)) {
            return "mix";
        }
        return target;
    }

    private String normalizeTopbarColor(String value) {
        String target = defaultText(value, DEFAULT_TOPBAR_COLOR);
        if ("fresh-green".equalsIgnoreCase(target)) {
            return DEFAULT_TOPBAR_COLOR;
        }
        if ("sky-blue".equalsIgnoreCase(target)) {
            return "linear-gradient(90deg, #4d81f7 0%, #5c92ff 100%)";
        }
        if ("vital-orange".equalsIgnoreCase(target)) {
            return "linear-gradient(90deg, #f59e0b 0%, #f97316 100%)";
        }
        if ("night-black".equalsIgnoreCase(target)) {
            return "linear-gradient(90deg, #111827 0%, #334155 100%)";
        }
        return target;
    }

    private String normalizeSidebarColor(String value) {
        String target = defaultText(value, DEFAULT_SIDEBAR_COLOR);
        if ("night-blue".equalsIgnoreCase(target)) {
            return DEFAULT_SIDEBAR_COLOR;
        }
        if ("forest-green".equalsIgnoreCase(target)) {
            return "linear-gradient(180deg, #163b2c 0%, #22543d 100%)";
        }
        if ("coffee-brown".equalsIgnoreCase(target)) {
            return "linear-gradient(180deg, #3b2f1f 0%, #5a472e 100%)";
        }
        if ("midnight-blue".equalsIgnoreCase(target)) {
            return "linear-gradient(180deg, #1f2937 0%, #111827 100%)";
        }
        return target;
    }
}
