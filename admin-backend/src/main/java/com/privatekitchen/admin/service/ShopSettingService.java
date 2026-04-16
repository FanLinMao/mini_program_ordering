package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.ShopSetting;

public interface ShopSettingService {

    ShopSetting getCurrentSetting();

    ShopSetting saveOrUpdateSetting(ShopSetting shopSetting);
}
