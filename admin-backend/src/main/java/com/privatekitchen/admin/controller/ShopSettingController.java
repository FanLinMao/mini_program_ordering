package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.ShopSetting;
import com.privatekitchen.admin.service.ShopSettingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop-settings")
public class ShopSettingController {

    private final ShopSettingService shopSettingService;

    public ShopSettingController(ShopSettingService shopSettingService) {
        this.shopSettingService = shopSettingService;
    }

    @GetMapping("/current")
    public ApiResponse<ShopSetting> getCurrentSetting() {
        return ApiResponse.success(shopSettingService.getCurrentSetting());
    }

    @PostMapping
    public ApiResponse<ShopSetting> saveOrUpdate(@RequestBody ShopSetting shopSetting) {
        return ApiResponse.success("保存成功", shopSettingService.saveOrUpdateSetting(shopSetting));
    }
}
