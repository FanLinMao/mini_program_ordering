package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.SysUser;
import com.privatekitchen.admin.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/users")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping
    public ApiResponse<List<SysUser>> listUsers() {
        return ApiResponse.success(sysUserService.listUsers());
    }

    @PostMapping
    public ApiResponse<SysUser> createUser(@RequestBody SysUser user) {
        return ApiResponse.success("保存成功", sysUserService.createUser(user));
    }

    @PutMapping("/{id}")
    public ApiResponse<SysUser> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        return ApiResponse.success("更新成功", sysUserService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return ApiResponse.success(true);
    }
}
