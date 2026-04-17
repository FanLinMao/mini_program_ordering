package com.privatekitchen.admin.miniapp.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.miniapp.dto.MiniappAddressSaveRequest;
import com.privatekitchen.admin.miniapp.service.MiniappProfileService;
import com.privatekitchen.admin.miniapp.vo.MiniappAddressVO;
import com.privatekitchen.admin.miniapp.vo.MiniappCouponVO;
import com.privatekitchen.admin.miniapp.vo.MiniappProfileHomeVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/miniapp/profile")
public class MiniappProfileController {

    private final MiniappProfileService miniappProfileService;

    public MiniappProfileController(MiniappProfileService miniappProfileService) {
        this.miniappProfileService = miniappProfileService;
    }

    @GetMapping("/home")
    public ApiResponse<MiniappProfileHomeVO> getProfileHome(@RequestParam(required = false) Long userId,
                                                            @RequestParam(required = false) String openid) {
        try {
            return ApiResponse.success(miniappProfileService.getProfileHome(userId, openid));
        } catch (IllegalArgumentException error) {
            return buildErrorResponse(400, error.getMessage(), null);
        } catch (Exception error) {
            return buildErrorResponse(500, resolveMessage(error, "加载个人中心失败，请稍后再试"), null);
        }
    }

    @GetMapping("/coupons")
    public ApiResponse<List<MiniappCouponVO>> listCoupons(@RequestParam(required = false) Long userId,
                                                          @RequestParam(required = false) String openid) {
        try {
            return ApiResponse.success(miniappProfileService.listCoupons(userId, openid));
        } catch (IllegalArgumentException error) {
            return buildErrorResponse(400, error.getMessage(), null);
        } catch (Exception error) {
            return buildErrorResponse(500, resolveMessage(error, "加载优惠券失败，请稍后再试"), null);
        }
    }

    @GetMapping("/addresses")
    public ApiResponse<List<MiniappAddressVO>> listAddresses(@RequestParam(required = false) Long userId,
                                                             @RequestParam(required = false) String openid) {
        try {
            return ApiResponse.success(miniappProfileService.listAddresses(userId, openid));
        } catch (IllegalArgumentException error) {
            return buildErrorResponse(400, error.getMessage(), null);
        } catch (Exception error) {
            return buildErrorResponse(500, resolveMessage(error, "加载地址失败，请稍后再试"), null);
        }
    }

    @PostMapping("/address")
    public ApiResponse<MiniappAddressVO> saveAddress(@RequestBody MiniappAddressSaveRequest request) {
        try {
            return ApiResponse.success("保存成功", miniappProfileService.saveAddress(request));
        } catch (IllegalArgumentException error) {
            return buildErrorResponse(400, error.getMessage(), null);
        } catch (Exception error) {
            return buildErrorResponse(500, resolveMessage(error, "保存地址失败，请稍后再试"), null);
        }
    }

    @PostMapping("/address/default/{id}")
    public ApiResponse<MiniappAddressVO> setDefaultAddress(@PathVariable("id") Long addressId,
                                                           @RequestParam(required = false) Long userId,
                                                           @RequestParam(required = false) String openid) {
        try {
            return ApiResponse.success("设置成功", miniappProfileService.setDefaultAddress(addressId, userId, openid));
        } catch (IllegalArgumentException error) {
            return buildErrorResponse(400, error.getMessage(), null);
        } catch (Exception error) {
            return buildErrorResponse(500, resolveMessage(error, "设置默认地址失败，请稍后再试"), null);
        }
    }

    @PostMapping("/address/delete/{id}")
    public ApiResponse<Boolean> deleteAddress(@PathVariable("id") Long addressId,
                                              @RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) String openid) {
        try {
            miniappProfileService.deleteAddress(addressId, userId, openid);
            return ApiResponse.success("删除成功", Boolean.TRUE);
        } catch (IllegalArgumentException error) {
            return buildErrorResponse(400, error.getMessage(), Boolean.FALSE);
        } catch (Exception error) {
            return buildErrorResponse(500, resolveMessage(error, "删除地址失败，请稍后再试"), Boolean.FALSE);
        }
    }

    private <T> ApiResponse<T> buildErrorResponse(int code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    private String resolveMessage(Exception error, String fallback) {
        return error.getMessage() == null || error.getMessage().isBlank() ? fallback : error.getMessage();
    }
}
