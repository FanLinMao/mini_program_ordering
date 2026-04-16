package com.privatekitchen.admin.miniapp.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.miniapp.service.MiniappMenuService;
import com.privatekitchen.admin.miniapp.vo.MiniappCategoryVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/miniapp/menu")
public class MiniappMenuController {

    private final MiniappMenuService miniappMenuService;

    public MiniappMenuController(MiniappMenuService miniappMenuService) {
        this.miniappMenuService = miniappMenuService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<MiniappCategoryVO>> listCategories() {
        return ApiResponse.success(miniappMenuService.listMenuCategories());
    }
}
