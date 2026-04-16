package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.dto.SysLoginRequest;
import com.privatekitchen.admin.dto.SysLogoutRequest;
import com.privatekitchen.admin.service.SysUserLoginLogService;
import com.privatekitchen.admin.service.SysUserService;
import com.privatekitchen.admin.vo.SysLoginUserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sys/auth")
public class SysAuthController {

    private final SysUserService sysUserService;
    private final SysUserLoginLogService sysUserLoginLogService;

    public SysAuthController(SysUserService sysUserService, SysUserLoginLogService sysUserLoginLogService) {
        this.sysUserService = sysUserService;
        this.sysUserLoginLogService = sysUserLoginLogService;
    }

    @PostMapping("/login")
    public ApiResponse<SysLoginUserVO> login(@RequestBody SysLoginRequest request, HttpServletRequest httpRequest) {
        SysLoginUserVO loginUser = sysUserService.login(request);
        if (loginUser == null) {
            sysUserLoginLogService.recordLog(
                    request == null ? null : request.getUsername(),
                    request == null ? null : request.getUsername(),
                    "LOGIN",
                    0,
                    resolveIp(httpRequest),
                    httpRequest.getHeader("User-Agent"),
                    "登录失败");
            ApiResponse<SysLoginUserVO> response = new ApiResponse<>();
            response.setCode(401);
            response.setMessage("账号或密码错误，或当前用户已停用");
            response.setData(null);
            return response;
        }

        sysUserLoginLogService.recordLog(
                loginUser.getUsername(),
                loginUser.getDisplayName(),
                "LOGIN",
                1,
                resolveIp(httpRequest),
                httpRequest.getHeader("User-Agent"),
                "登录成功");
        return ApiResponse.success("登录成功", loginUser);
    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(@RequestBody SysLogoutRequest request, HttpServletRequest httpRequest) {
        boolean success = sysUserService.logout(request);
        sysUserLoginLogService.recordLog(
                request == null ? null : request.getUsername(),
                request == null ? null : request.getUsername(),
                "LOGOUT",
                success ? 1 : 0,
                resolveIp(httpRequest),
                httpRequest.getHeader("User-Agent"),
                success ? "退出系统" : "退出失败");

        if (!success) {
            ApiResponse<Boolean> response = new ApiResponse<>();
            response.setCode(401);
            response.setMessage("退出失败，当前登录状态已失效");
            response.setData(false);
            return response;
        }
        return ApiResponse.success("退出成功", true);
    }

    private String resolveIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
