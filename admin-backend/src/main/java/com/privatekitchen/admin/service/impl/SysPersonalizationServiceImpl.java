package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.SysPersonalizationDao;
import com.privatekitchen.admin.entity.SysPersonalization;
import com.privatekitchen.admin.service.SysPersonalizationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysPersonalizationServiceImpl implements SysPersonalizationService {

    private final SysPersonalizationDao sysPersonalizationDao;

    public SysPersonalizationServiceImpl(SysPersonalizationDao sysPersonalizationDao) {
        this.sysPersonalizationDao = sysPersonalizationDao;
    }

    @Override
    public SysPersonalization getConfig() {
        List<SysPersonalization> list = sysPersonalizationDao.selectList(baseQuery());
        if (list.isEmpty()) {
            SysPersonalization config = buildDefaultConfig();
            sysPersonalizationDao.insert(config);
            return config;
        }
        return list.get(0);
    }

    @Override
    public SysPersonalization saveConfig(SysPersonalization config) {
        SysPersonalization existing = getConfig();
        existing.setLogoUrl(config.getLogoUrl());
        existing.setSystemName(defaultText(config.getSystemName(), existing.getSystemName()));
        existing.setSystemIntro(defaultText(config.getSystemIntro(), existing.getSystemIntro()));
        existing.setWelcomeText(defaultText(config.getWelcomeText(), existing.getWelcomeText()));
        existing.setSystemBackgroundImage(config.getSystemBackgroundImage());
        existing.setLoginBackgroundImage(config.getLoginBackgroundImage());
        existing.setThemeColor(defaultText(config.getThemeColor(), existing.getThemeColor()));
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysPersonalizationDao.updateById(existing);
        return existing;
    }

    private LambdaQueryWrapper<SysPersonalization> baseQuery() {
        return new LambdaQueryWrapper<SysPersonalization>()
                .eq(SysPersonalization::getDelFlag, 0)
                .orderByAsc(SysPersonalization::getCreateTime);
    }

    private SysPersonalization buildDefaultConfig() {
        LocalDateTime now = LocalDateTime.now();
        SysPersonalization config = new SysPersonalization();
        config.setId(2045000000000000051L);
        config.setLogoUrl("");
        config.setSystemName("Kitchen Admin");
        config.setSystemIntro("私人厨房后台管理");
        config.setWelcomeText("欢迎使用私人厨房后台管理系统");
        config.setSystemBackgroundImage("");
        config.setLoginBackgroundImage("");
        config.setThemeColor("#409EFF");
        config.setDelFlag(0);
        config.setCreateTime(now);
        config.setCreateBy("admin");
        config.setUpdateTime(now);
        config.setUpdateBy("admin");
        return config;
    }

    private String defaultText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
