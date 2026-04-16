package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.SysUserThemeSetting;

public interface SysUserThemeSettingService {

    SysUserThemeSetting getCurrentSetting(String userCode);

    SysUserThemeSetting saveOrUpdateSetting(SysUserThemeSetting setting);
}
