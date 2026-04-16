package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.dto.SysChangePasswordRequest;
import com.privatekitchen.admin.entity.SysUser;
import com.privatekitchen.admin.entity.SysUserLoginLog;
import com.privatekitchen.admin.service.SysUserLoginLogService;
import com.privatekitchen.admin.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/users")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysUserLoginLogService sysUserLoginLogService;

    public SysUserController(SysUserService sysUserService, SysUserLoginLogService sysUserLoginLogService) {
        this.sysUserService = sysUserService;
        this.sysUserLoginLogService = sysUserLoginLogService;
    }

    @GetMapping
    public ApiResponse<List<SysUser>> listUsers() {
        return ApiResponse.success(sysUserService.listUsers());
    }

    @GetMapping("/current")
    public ApiResponse<SysUser> getCurrentUser(
            @RequestParam(value = "username", required = false, defaultValue = "admin") String username) {
        return ApiResponse.success(sysUserService.getCurrentUser(username));
    }

    @PutMapping("/current/profile")
    public ApiResponse<SysUser> updateCurrentProfile(
            @RequestParam(value = "username", required = false, defaultValue = "admin") String username,
            @RequestBody SysUser user,
            HttpServletRequest request) {
        SysUser updated = sysUserService.updateCurrentUser(username, user);
        if (updated != null) {
            sysUserLoginLogService.recordLog(
                    username,
                    updated.getDisplayName(),
                    "UPDATE_PROFILE",
                    1,
                    resolveIp(request),
                    request.getHeader("User-Agent"),
                    "更新个人资料");
        }
        return ApiResponse.success("更新成功", updated);
    }

    @PutMapping("/current/password")
    public ApiResponse<Boolean> changePassword(
            @RequestParam(value = "username", required = false, defaultValue = "admin") String username,
            @RequestBody SysChangePasswordRequest request,
            HttpServletRequest httpRequest) {
        boolean success = sysUserService.changePassword(username, request.getOldPassword(), request.getNewPassword());
        sysUserLoginLogService.recordLog(
                username,
                username,
                "CHANGE_PASSWORD",
                success ? 1 : 0,
                resolveIp(httpRequest),
                httpRequest.getHeader("User-Agent"),
                success ? "修改登录密码" : "修改密码失败");

        if (!success) {
            ApiResponse<Boolean> response = new ApiResponse<>();
            response.setCode(400);
            response.setMessage("原密码不正确或新密码无效");
            response.setData(false);
            return response;
        }
        return ApiResponse.success("密码修改成功", true);
    }

    @GetMapping("/current/login-logs")
    public ApiResponse<List<SysUserLoginLog>> listCurrentLoginLogs(
            @RequestParam(value = "username", required = false, defaultValue = "admin") String username) {
        return ApiResponse.success(sysUserService.listCurrentUserLogs(username));
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

    private String resolveIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
