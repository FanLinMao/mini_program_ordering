package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.ShopSettingDao;
import com.privatekitchen.admin.entity.ShopSetting;
import com.privatekitchen.admin.service.ShopSettingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShopSettingServiceImpl implements ShopSettingService {

    private final ShopSettingDao shopSettingDao;

    public ShopSettingServiceImpl(ShopSettingDao shopSettingDao) {
        this.shopSettingDao = shopSettingDao;
    }

    @Override
    public ShopSetting getCurrentSetting() {
        return shopSettingDao.selectOne(new LambdaQueryWrapper<ShopSetting>()
                .eq(ShopSetting::getDelFlag, 0)
                .last("limit 1"));
    }

    @Override
    public ShopSetting saveOrUpdateSetting(ShopSetting shopSetting) {
        LocalDateTime now = LocalDateTime.now();
        if (shopSetting.getId() == null) {
            shopSetting.setDelFlag(0);
            shopSetting.setCreateTime(now);
            shopSetting.setCreateBy("admin");
            shopSetting.setUpdateTime(now);
            shopSetting.setUpdateBy("admin");
            shopSettingDao.insert(shopSetting);
            return shopSetting;
        }

        ShopSetting existing = shopSettingDao.selectById(shopSetting.getId());
        existing.setShopName(shopSetting.getShopName());
        existing.setAddress(shopSetting.getAddress());
        existing.setNotice(shopSetting.getNotice());
        existing.setBusinessHours(shopSetting.getBusinessHours());
        existing.setTakeoutEnabled(shopSetting.getTakeoutEnabled());
        existing.setCookNowEnabled(shopSetting.getCookNowEnabled());
        existing.setWxpayEnabled(shopSetting.getWxpayEnabled());
        existing.setUpdateTime(now);
        existing.setUpdateBy("admin");
        shopSettingDao.updateById(existing);
        return existing;
    }
}
