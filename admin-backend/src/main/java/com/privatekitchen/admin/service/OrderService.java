package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.Order;
import com.privatekitchen.admin.vo.OrderDetailVO;

import java.util.List;

public interface OrderService {

    List<Order> listOrders();

    OrderDetailVO getOrderDetail(Long id);

    Order saveOrder(Order order);

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);
}
