package com.privatekitchen.admin.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.privatekitchen.admin.dao.DishDao;
import com.privatekitchen.admin.dao.OrderDao;
import com.privatekitchen.admin.dao.OrderItemDao;
import com.privatekitchen.admin.dao.ShopSettingDao;
import com.privatekitchen.admin.dao.UserDao;
import com.privatekitchen.admin.entity.Dish;
import com.privatekitchen.admin.entity.Order;
import com.privatekitchen.admin.entity.OrderItem;
import com.privatekitchen.admin.entity.ShopSetting;
import com.privatekitchen.admin.entity.User;
import com.privatekitchen.admin.miniapp.dto.MiniappSubmitOrderItemRequest;
import com.privatekitchen.admin.miniapp.dto.MiniappSubmitOrderRequest;
import com.privatekitchen.admin.miniapp.service.MiniappOrderService;
import com.privatekitchen.admin.miniapp.vo.MiniappSubmitOrderItemVO;
import com.privatekitchen.admin.miniapp.vo.MiniappSubmitOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MiniappOrderServiceImpl implements MiniappOrderService {

    private static final DateTimeFormatter ORDER_NO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String DEFAULT_PICKUP_TYPE = "到店自取";
    private static final String DEFAULT_NOTE = "请在 15 分钟内完成支付";
    private static final String DEFAULT_STATUS = "pendingPay";
    private static final String DEFAULT_STATUS_TEXT = "待支付";

    private final UserDao userDao;
    private final DishDao dishDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final ShopSettingDao shopSettingDao;

    public MiniappOrderServiceImpl(
            UserDao userDao,
            DishDao dishDao,
            OrderDao orderDao,
            OrderItemDao orderItemDao,
            ShopSettingDao shopSettingDao) {
        this.userDao = userDao;
        this.dishDao = dishDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.shopSettingDao = shopSettingDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MiniappSubmitOrderVO submitCookNowOrder(MiniappSubmitOrderRequest request) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("下单商品不能为空");
        }

        User currentUser = resolveUser(request);
        if (currentUser == null) {
            throw new IllegalArgumentException("未找到下单用户");
        }

        Map<Long, Integer> dishCountMap = request.getItems().stream()
                .filter(item -> item.getId() != null && item.getCount() != null && item.getCount() > 0)
                .collect(Collectors.toMap(
                        MiniappSubmitOrderItemRequest::getId,
                        MiniappSubmitOrderItemRequest::getCount,
                        Integer::sum,
                        LinkedHashMap::new
                ));

        if (dishCountMap.isEmpty()) {
            throw new IllegalArgumentException("下单商品不能为空");
        }

        List<Dish> dishes = dishDao.selectList(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getDelFlag, 0)
                .eq(Dish::getStatus, 1)
                .in(Dish::getId, dishCountMap.keySet())
                .orderByAsc(Dish::getSort)
                .orderByAsc(Dish::getId));

        if (dishes.size() != dishCountMap.size()) {
            throw new IllegalArgumentException("存在已下架或不存在的菜品");
        }

        ShopSetting shopSetting = resolveShopSetting();
        LocalDateTime now = LocalDateTime.now();
        Long orderId = IdWorker.getId();
        String orderNo = "COOK" + ORDER_NO_FORMATTER.format(now);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Dish dish : dishes) {
            Integer count = dishCountMap.get(dish.getId());
            BigDecimal itemAmount = dish.getPrice().multiply(BigDecimal.valueOf(count));
            totalAmount = totalAmount.add(itemAmount);
        }
        totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);

        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo(orderNo);
        order.setUserId(currentUser.getId());
        order.setShopName(shopSetting != null ? shopSetting.getShopName() : "私人厨房");
        order.setPickupType(DEFAULT_PICKUP_TYPE);
        order.setContactName(defaultText(currentUser.getNickname(), "微信用户"));
        order.setAmount(totalAmount);
        order.setStatus(DEFAULT_STATUS);
        order.setStatusText(DEFAULT_STATUS_TEXT);
        order.setNote(DEFAULT_NOTE);
        order.setOrderType("miniapp");
        order.setPayType("cookNow");
        order.setDelFlag(0);
        order.setCreateTime(now);
        order.setCreateBy(String.valueOf(currentUser.getId()));
        order.setUpdateTime(now);
        order.setUpdateBy(String.valueOf(currentUser.getId()));
        orderDao.insert(order);

        MiniappSubmitOrderVO result = new MiniappSubmitOrderVO();
        result.setOrderId(String.valueOf(orderId));
        result.setOrderNo(orderNo);
        result.setAmount(totalAmount);
        result.setStatus(DEFAULT_STATUS);
        result.setStatusText(DEFAULT_STATUS_TEXT);
        result.setNote(DEFAULT_NOTE);
        result.setShopName(order.getShopName());
        result.setPickupType(order.getPickupType());
        result.setContactName(order.getContactName());
        result.setPayType(order.getPayType());
        result.setTime("刚刚下单");

        List<MiniappSubmitOrderItemVO> orderItems = dishes.stream()
                .map(dish -> buildOrderItem(orderId, currentUser.getId(), dish, dishCountMap.get(dish.getId()), now))
                .collect(Collectors.toList());
        result.setItems(orderItems);
        result.setTitle(buildOrderTitle(orderItems));

        return result;
    }

    private MiniappSubmitOrderItemVO buildOrderItem(Long orderId, Long userId, Dish dish, Integer count, LocalDateTime now) {
        BigDecimal itemAmount = dish.getPrice().multiply(BigDecimal.valueOf(count)).setScale(2, RoundingMode.HALF_UP);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(IdWorker.getId());
        orderItem.setOrderId(orderId);
        orderItem.setDishId(dish.getId());
        orderItem.setDishName(dish.getName());
        orderItem.setPrice(dish.getPrice());
        orderItem.setCount(count);
        orderItem.setAmount(itemAmount);
        orderItem.setDelFlag(0);
        orderItem.setCreateTime(now);
        orderItem.setCreateBy(String.valueOf(userId));
        orderItem.setUpdateTime(now);
        orderItem.setUpdateBy(String.valueOf(userId));
        orderItemDao.insert(orderItem);

        MiniappSubmitOrderItemVO result = new MiniappSubmitOrderItemVO();
        result.setId(String.valueOf(dish.getId()));
        result.setName(dish.getName());
        result.setCount(count);
        result.setPrice(dish.getPrice());
        return result;
    }

    private String buildOrderTitle(List<MiniappSubmitOrderItemVO> items) {
        int totalCount = items.stream()
                .map(MiniappSubmitOrderItemVO::getCount)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
        String title = items.stream()
                .limit(2)
                .map(MiniappSubmitOrderItemVO::getName)
                .collect(Collectors.joining(" + "));
        if (totalCount > 2) {
            return title + " 等" + totalCount + "件商品";
        }
        return title;
    }

    private User resolveUser(MiniappSubmitOrderRequest request) {
        if (request.getUserId() != null) {
            User user = userDao.selectById(request.getUserId());
            if (user != null && Integer.valueOf(0).equals(user.getDelFlag()) && Integer.valueOf(1).equals(user.getStatus())) {
                return user;
            }
        }

        if (request.getOpenid() != null && !request.getOpenid().isBlank()) {
            User user = userDao.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getDelFlag, 0)
                    .eq(User::getStatus, 1)
                    .eq(User::getOpenid, request.getOpenid())
                    .last("LIMIT 1"));
            if (user != null) {
                return user;
            }
        }

        return userDao.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getDelFlag, 0)
                .eq(User::getStatus, 1)
                .orderByAsc(User::getId)
                .last("LIMIT 1"));
    }

    private ShopSetting resolveShopSetting() {
        return shopSettingDao.selectOne(new LambdaQueryWrapper<ShopSetting>()
                .eq(ShopSetting::getDelFlag, 0)
                .orderByAsc(ShopSetting::getId)
                .last("LIMIT 1"));
    }

    private String defaultText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
