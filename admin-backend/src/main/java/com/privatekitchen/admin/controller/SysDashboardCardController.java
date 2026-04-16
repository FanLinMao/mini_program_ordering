package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.SysDashboardCard;
import com.privatekitchen.admin.service.SysDashboardCardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/dashboard-cards")
public class SysDashboardCardController {

    private final SysDashboardCardService sysDashboardCardService;

    public SysDashboardCardController(SysDashboardCardService sysDashboardCardService) {
        this.sysDashboardCardService = sysDashboardCardService;
    }

    @GetMapping
    public ApiResponse<List<SysDashboardCard>> listCards() {
        return ApiResponse.success(sysDashboardCardService.listCards());
    }

    @PostMapping
    public ApiResponse<SysDashboardCard> createCard(@RequestBody SysDashboardCard card) {
        return ApiResponse.success("保存成功", sysDashboardCardService.createCard(card));
    }

    @PutMapping("/{id}")
    public ApiResponse<SysDashboardCard> updateCard(@PathVariable Long id, @RequestBody SysDashboardCard card) {
        return ApiResponse.success("更新成功", sysDashboardCardService.updateCard(id, card));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteCard(@PathVariable Long id) {
        sysDashboardCardService.deleteCard(id);
        return ApiResponse.success(true);
    }
}
