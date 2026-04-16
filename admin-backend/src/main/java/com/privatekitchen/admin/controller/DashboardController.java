package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.dao.OrderDao;
import com.privatekitchen.admin.dao.UserDao;
import com.privatekitchen.admin.entity.Order;
import com.privatekitchen.admin.entity.SysDashboardCard;
import com.privatekitchen.admin.entity.User;
import com.privatekitchen.admin.service.SysDashboardCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final SysDashboardCardService sysDashboardCardService;

    public DashboardController(OrderDao orderDao, UserDao userDao, SysDashboardCardService sysDashboardCardService) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.sysDashboardCardService = sysDashboardCardService;
    }

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> getOverview() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Order> orders = orderDao.selectList(null).stream()
                .filter(order -> order.getDelFlag() == null || order.getDelFlag() == 0)
                .toList();
        List<User> users = userDao.selectList(null).stream()
                .filter(user -> user.getDelFlag() == null || user.getDelFlag() == 0)
                .toList();
        List<SysDashboardCard> dashboardCards = sysDashboardCardService.listEnabledCards();

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        long todayOrderCount = orders.stream()
                .filter(order -> isSameDay(order.getCreateTime(), today))
                .count();
        long yesterdayOrderCount = orders.stream()
                .filter(order -> isSameDay(order.getCreateTime(), yesterday))
                .count();
        long pendingPickupCount = orders.stream()
                .filter(order -> "待取餐".equals(order.getStatusText()))
                .count();
        long newUserCount = users.stream()
                .filter(user -> isSameDay(user.getCreateTime(), today))
                .count();

        BigDecimal todayRevenue = orders.stream()
                .filter(order -> isSameDay(order.getCreateTime(), today))
                .map(Order::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long cookNowOrderCount = orders.stream()
                .filter(order -> "cookNow".equals(order.getPayType()))
                .count();
        long pendingPayCount = orders.stream()
                .filter(order -> "待支付".equals(order.getStatusText()))
                .count();
        long refundCount = orders.stream()
                .filter(order -> "退款售后".equals(order.getStatusText()))
                .count();

        long orderGrowth = yesterdayOrderCount == 0
                ? 100
                : Math.round(((double) (todayOrderCount - yesterdayOrderCount) / yesterdayOrderCount) * 100);

        long cookNowRatio = orders.isEmpty() ? 0 : Math.round((double) cookNowOrderCount / orders.size() * 100);

        result.put("metrics", dashboardCards.isEmpty()
                ? List.of(
                metric("今日订单", String.valueOf(todayOrderCount), "较昨日 " + formatTrend(orderGrowth)),
                metric("待取餐订单", String.valueOf(pendingPickupCount), "高峰时段需重点关注"),
                metric("今日营业额", "¥" + todayRevenue.toPlainString(), "现炒下单占比 " + cookNowRatio + "%"),
                metric("会员新增", String.valueOf(newUserCount), "用户数据持续沉淀")
        )
                : dashboardCards.stream()
                .map(card -> metric(card.getTitle(), card.getCardValue(), card.getExtraText()))
                .toList());

        result.put("tasks", List.of(
                task("待支付订单提醒", "及时关注未支付超时单，减少流失。", pendingPayCount + " 单"),
                task("待取餐订单处理", "有订单即将达到预计出餐时间。", pendingPickupCount + " 单"),
                task("退款售后复核", "售后单建议在 10 分钟内响应。", refundCount + " 单")
        ));

        result.put("tips", List.of(
                "优先完善菜品、分类、订单、店铺设置四类后台核心流程。",
                "小程序现炒下单与微信支付建议统一纳入订单主表，便于统计和追踪。",
                "系统设置中的首页卡片、公告、菜单已经独立拆分，后续维护会更清晰。"
        ));

        return ApiResponse.success(result);
    }

    private boolean isSameDay(LocalDateTime dateTime, LocalDate date) {
        return dateTime != null && dateTime.toLocalDate().isEqual(date);
    }

    private String formatTrend(long percent) {
        return (percent >= 0 ? "+" : "") + percent + "%";
    }

    private Map<String, String> metric(String label, String value, String extra) {
        Map<String, String> metric = new LinkedHashMap<>();
        metric.put("label", label);
        metric.put("value", value);
        metric.put("extra", extra == null ? "" : extra);
        return metric;
    }

    private Map<String, String> task(String title, String desc, String count) {
        Map<String, String> task = new LinkedHashMap<>();
        task.put("title", title);
        task.put("desc", desc);
        task.put("count", count);
        return task;
    }
}
