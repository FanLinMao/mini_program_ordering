package com.privatekitchen.admin.miniapp.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.miniapp.dto.MiniappLoginRequest;
import com.privatekitchen.admin.miniapp.dto.MiniappLogoutRequest;
import com.privatekitchen.admin.miniapp.service.MiniappAuthService;
import com.privatekitchen.admin.miniapp.vo.MiniappLoginUserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/miniapp/auth")
public class MiniappAuthController {

    private final MiniappAuthService miniappAuthService;

    public MiniappAuthController(MiniappAuthService miniappAuthService) {
        this.miniappAuthService = miniappAuthService;
    }

    @PostMapping("/login")
    public ApiResponse<MiniappLoginUserVO> login(@RequestBody MiniappLoginRequest request) {
        try {
            return ApiResponse.success("登录成功", miniappAuthService.login(request));
        } catch (IllegalArgumentException error) {
            ApiResponse<MiniappLoginUserVO> response = new ApiResponse<>();
            response.setCode(400);
            response.setMessage(error.getMessage());
            response.setData(null);
            return response;
        } catch (Exception error) {
            ApiResponse<MiniappLoginUserVO> response = new ApiResponse<>();
            response.setCode(500);
            response.setMessage(error.getMessage() == null || error.getMessage().isBlank()
                    ? "登录失败，请稍后再试"
                    : error.getMessage());
            response.setData(null);
            return response;
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(@RequestBody MiniappLogoutRequest request) {
        try {
            miniappAuthService.logout(request);
            return ApiResponse.success("退出登录成功", Boolean.TRUE);
        } catch (IllegalArgumentException error) {
            ApiResponse<Boolean> response = new ApiResponse<>();
            response.setCode(400);
            response.setMessage(error.getMessage());
            response.setData(Boolean.FALSE);
            return response;
        } catch (Exception error) {
            ApiResponse<Boolean> response = new ApiResponse<>();
            response.setCode(500);
            response.setMessage(error.getMessage() == null || error.getMessage().isBlank()
                    ? "退出登录失败，请稍后再试"
                    : error.getMessage());
            response.setData(Boolean.FALSE);
            return response;
        }
    }
}
