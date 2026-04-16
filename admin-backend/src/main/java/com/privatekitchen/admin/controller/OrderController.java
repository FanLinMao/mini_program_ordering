package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.Order;
import com.privatekitchen.admin.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ApiResponse<List<Order>> listOrders() {
        return ApiResponse.success(orderService.listOrders());
    }

    @PostMapping
    public ApiResponse<Order> saveOrder(@RequestBody Order order) {
        return ApiResponse.success("新增成功", orderService.saveOrder(order));
    }

    @PutMapping("/{id}")
    public ApiResponse<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return ApiResponse.success("更新成功", orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ApiResponse.success("删除成功", null);
    }
}
