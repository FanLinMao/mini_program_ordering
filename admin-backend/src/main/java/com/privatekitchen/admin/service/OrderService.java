package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> listOrders();

    Order saveOrder(Order order);

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);
}
