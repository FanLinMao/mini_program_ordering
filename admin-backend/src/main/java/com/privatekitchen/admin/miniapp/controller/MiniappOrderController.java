package com.privatekitchen.admin.miniapp.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.miniapp.dto.MiniappSubmitOrderRequest;
import com.privatekitchen.admin.miniapp.service.MiniappOrderService;
import com.privatekitchen.admin.miniapp.vo.MiniappSubmitOrderVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/miniapp/orders")
public class MiniappOrderController {

    private final MiniappOrderService miniappOrderService;

    public MiniappOrderController(MiniappOrderService miniappOrderService) {
        this.miniappOrderService = miniappOrderService;
    }

    @PostMapping("/cook-now")
    public ApiResponse<MiniappSubmitOrderVO> submitCookNowOrder(@RequestBody MiniappSubmitOrderRequest request) {
        try {
            return ApiResponse.success("下单成功", miniappOrderService.submitCookNowOrder(request));
        } catch (IllegalArgumentException error) {
            ApiResponse<MiniappSubmitOrderVO> response = new ApiResponse<>();
            response.setCode(400);
            response.setMessage(error.getMessage());
            response.setData(null);
            return response;
        } catch (Exception error) {
            ApiResponse<MiniappSubmitOrderVO> response = new ApiResponse<>();
            response.setCode(500);
            response.setMessage(error.getMessage() == null || error.getMessage().isBlank()
                    ? "下单失败，请稍后再试"
                    : error.getMessage());
            response.setData(null);
            return response;
        }
    }
}
