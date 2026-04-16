package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.SysMenu;
import com.privatekitchen.admin.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/menus")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @GetMapping
    public ApiResponse<List<SysMenu>> listMenuTree() {
        return ApiResponse.success(sysMenuService.listMenuTree());
    }

    @GetMapping("/sidebar")
    public ApiResponse<List<SysMenu>> listSidebarMenus() {
        return ApiResponse.success(sysMenuService.listSidebarMenus());
    }

    @PostMapping
    public ApiResponse<SysMenu> createMenu(@RequestBody SysMenu menu) {
        return ApiResponse.success("保存成功", sysMenuService.createMenu(menu));
    }

    @PutMapping("/{id}")
    public ApiResponse<SysMenu> updateMenu(@PathVariable Long id, @RequestBody SysMenu menu) {
        return ApiResponse.success("更新成功", sysMenuService.updateMenu(id, menu));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteMenu(@PathVariable Long id) {
        sysMenuService.deleteMenu(id);
        return ApiResponse.success(true);
    }
}
