package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.DishDao;
import com.privatekitchen.admin.dao.OrderDao;
import com.privatekitchen.admin.dao.OrderItemDao;
import com.privatekitchen.admin.entity.Dish;
import com.privatekitchen.admin.entity.Order;
import com.privatekitchen.admin.entity.OrderItem;
import com.privatekitchen.admin.service.OrderService;
import com.privatekitchen.admin.vo.OrderDetailItemVO;
import com.privatekitchen.admin.vo.OrderDetailVO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final DishDao dishDao;

    public OrderServiceImpl(OrderDao orderDao, OrderItemDao orderItemDao, DishDao dishDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.dishDao = dishDao;
    }

    @Override
    public List<Order> listOrders() {
        return orderDao.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getDelFlag, 0)
                .orderByDesc(Order::getCreateTime));
    }

    @Override
    public OrderDetailVO getOrderDetail(Long id) {
        Order order = orderDao.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, id)
                .eq(Order::getDelFlag, 0)
                .last("LIMIT 1"));
        if (order == null) {
            return null;
        }

        List<OrderItem> orderItems = orderItemDao.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, id)
                .eq(OrderItem::getDelFlag, 0)
                .orderByAsc(OrderItem::getCreateTime)
                .orderByAsc(OrderItem::getId));
        Map<Long, String> dishImageMap = buildDishImageMap(orderItems);

        OrderDetailVO detail = new OrderDetailVO();
        detail.setId(order.getId());
        detail.setOrderNo(order.getOrderNo());
        detail.setUserId(order.getUserId());
        detail.setShopName(order.getShopName());
        detail.setPickupType(order.getPickupType());
        detail.setContactName(order.getContactName());
        detail.setAmount(order.getAmount());
        detail.setStatus(order.getStatus());
        detail.setStatusText(order.getStatusText());
        detail.setNote(order.getNote());
        detail.setOrderType(order.getOrderType());
        detail.setPayType(order.getPayType());
        detail.setCreateTime(order.getCreateTime());
        detail.setCreateBy(order.getCreateBy());
        detail.setUpdateTime(order.getUpdateTime());
        detail.setUpdateBy(order.getUpdateBy());
        detail.setItems(orderItems.stream().map(item -> mapDetailItem(item, dishImageMap)).collect(Collectors.toList()));
        return detail;
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

    private Map<Long, String> buildDishImageMap(List<OrderItem> orderItems) {
        Set<Long> dishIds = orderItems.stream()
                .map(OrderItem::getDishId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (dishIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return dishDao.selectList(new LambdaQueryWrapper<Dish>()
                        .in(Dish::getId, dishIds)
                        .eq(Dish::getDelFlag, 0))
                .stream()
                .collect(Collectors.toMap(Dish::getId, Dish::getImage, (left, right) -> left));
    }

    private OrderDetailItemVO mapDetailItem(OrderItem item, Map<Long, String> dishImageMap) {
        OrderDetailItemVO detailItem = new OrderDetailItemVO();
        detailItem.setId(item.getId());
        detailItem.setDishId(item.getDishId());
        detailItem.setDishName(item.getDishName());
        detailItem.setDishImage(dishImageMap.getOrDefault(item.getDishId(), ""));
        detailItem.setPrice(item.getPrice());
        detailItem.setCount(item.getCount());
        detailItem.setAmount(item.getAmount());
        return detailItem;
    }
}
