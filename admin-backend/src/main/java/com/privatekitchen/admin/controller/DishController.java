package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.Dish;
import com.privatekitchen.admin.service.DishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ApiResponse<List<Dish>> listDishes() {
        return ApiResponse.success(dishService.listDishes());
    }

    @PostMapping
    public ApiResponse<Dish> saveDish(@RequestBody Dish dish) {
        return ApiResponse.success("新增成功", dishService.saveDish(dish));
    }

    @PutMapping("/{id}")
    public ApiResponse<Dish> updateDish(@PathVariable Long id, @RequestBody Dish dish) {
        return ApiResponse.success("更新成功", dishService.updateDish(id, dish));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ApiResponse.success("删除成功", null);
    }
}
