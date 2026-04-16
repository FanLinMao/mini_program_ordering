package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.OrderDao;
import com.privatekitchen.admin.entity.Order;
import com.privatekitchen.admin.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<Order> listOrders() {
        return orderDao.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getDelFlag, 0)
                .orderByDesc(Order::getCreateTime));
    }

    @Override
    public Order saveOrder(Order order) {
        LocalDateTime now = LocalDateTime.now();
        order.setDelFlag(0);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        order.setCreateBy("admin");
        order.setUpdateBy("admin");
        orderDao.insert(order);
        return order;
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order existing = orderDao.selectById(id);
        existing.setOrderNo(order.getOrderNo());
        existing.setUserId(order.getUserId());
        existing.setShopName(order.getShopName());
        existing.setPickupType(order.getPickupType());
        existing.setContactName(order.getContactName());
        existing.setAmount(order.getAmount());
        existing.setStatus(order.getStatus());
        existing.setStatusText(order.getStatusText());
        existing.setNote(order.getNote());
        existing.setOrderType(order.getOrderType());
        existing.setPayType(order.getPayType());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        orderDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteOrder(Long id) {
        Order existing = orderDao.selectById(id);
        if (existing == null) {
            return;
        }
        existing.setDelFlag(1);
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        orderDao.updateById(existing);
    }
}
