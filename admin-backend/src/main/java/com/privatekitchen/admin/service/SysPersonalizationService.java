package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.SysPersonalization;

public interface SysPersonalizationService {

    SysPersonalization getConfig();

    SysPersonalization saveConfig(SysPersonalization config);
}
