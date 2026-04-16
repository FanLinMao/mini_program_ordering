package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.dto.SysLoginRequest;
import com.privatekitchen.admin.service.SysUserService;
import com.privatekitchen.admin.vo.SysLoginUserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sys/auth")
public class SysAuthController {

    private final SysUserService sysUserService;

    public SysAuthController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/login")
    public ApiResponse<SysLoginUserVO> login(@RequestBody SysLoginRequest request) {
        SysLoginUserVO loginUser = sysUserService.login(request);
        if (loginUser == null) {
            ApiResponse<SysLoginUserVO> response = new ApiResponse<>();
            response.setCode(401);
            response.setMessage("账号或密码错误，或当前用户已停用");
            response.setData(null);
            return response;
        }
        return ApiResponse.success("登录成功", loginUser);
    }
}
