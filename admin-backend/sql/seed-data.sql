USE private_kitchen;

INSERT INTO categories (
    id, name, sort, status, del_flag, create_time, create_by, update_time, update_by
) VALUES
    (1001, '热销', 1, 1, 0, '2026-04-14 09:00:00', 'admin', '2026-04-14 09:00:00', 'admin'),
    (1002, '家常菜', 2, 1, 0, '2026-04-14 09:01:00', 'admin', '2026-04-14 09:01:00', 'admin'),
    (1003, '米饭', 3, 1, 0, '2026-04-14 09:02:00', 'admin', '2026-04-14 09:02:00', 'admin'),
    (1004, '酒水饮料', 4, 1, 0, '2026-04-14 09:03:00', 'admin', '2026-04-14 09:03:00', 'admin')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    sort = VALUES(sort),
    status = VALUES(status),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO dishes (
    id, category_id, name, price, image, monthly_sales, praise, extra, sort, status, del_flag,
    create_time, create_by, update_time, update_by
) VALUES
    (2001, 1001, '盐煎肉(不含白米饭)', 22.00, '/static/dishes/salt-fried-pork.jpg', 200, 50, '50+ 回头客推荐', 1, 1, 0, '2026-04-14 09:10:00', 'admin', '2026-04-14 09:10:00', 'admin'),
    (2002, 1002, '鱼香肉丝', 17.00, '/static/dishes/fish-flavored-pork.jpg', 61, 20, '20+ 回头客推荐', 2, 1, 0, '2026-04-14 09:11:00', 'admin', '2026-04-14 09:11:00', 'admin'),
    (2003, 1002, '泡椒鸡杂', 17.00, '/static/dishes/pickled-pepper-giblets.jpg', 82, 30, '30+ 回头客推荐', 3, 1, 0, '2026-04-14 09:12:00', 'admin', '2026-04-14 09:12:00', 'admin'),
    (2004, 1002, '番茄炒蛋', 15.00, '/static/dishes/tomato-eggs.jpg', 62, 10, '经典家常下饭菜', 4, 1, 0, '2026-04-14 09:13:00', 'admin', '2026-04-14 09:13:00', 'admin'),
    (2005, 1003, '白米饭', 2.00, '/static/dishes/rice.jpg', 320, 0, '搭配主菜更香', 5, 1, 0, '2026-04-14 09:14:00', 'admin', '2026-04-14 09:14:00', 'admin'),
    (2006, 1004, '冰红茶', 5.00, '/static/dishes/iced-tea.jpg', 30, 0, '解腻推荐', 6, 1, 0, '2026-04-14 09:15:00', 'admin', '2026-04-14 09:15:00', 'admin')
ON DUPLICATE KEY UPDATE
    category_id = VALUES(category_id),
    name = VALUES(name),
    price = VALUES(price),
    image = VALUES(image),
    monthly_sales = VALUES(monthly_sales),
    praise = VALUES(praise),
    extra = VALUES(extra),
    sort = VALUES(sort),
    status = VALUES(status),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO users (
    id, openid, nickname, phone, avatar, points, member_level, status, del_flag,
    create_time, create_by, update_time, update_by
) VALUES
    (3001, 'oAbcUser001', '用餐小王', '13800000011', 'https://example.com/avatar1.png', 1260, 'VIP', 1, 0, '2026-04-14 09:20:00', 'admin', '2026-04-14 09:20:00', 'admin'),
    (3002, 'oAbcUser002', '爱吃川菜', '13900000022', 'https://example.com/avatar2.png', 620, '银卡', 1, 0, '2026-04-14 09:21:00', 'admin', '2026-04-14 09:21:00', 'admin'),
    (3003, 'oAbcUser003', '午餐搭子', '13700000033', 'https://example.com/avatar3.png', 300, '普通', 1, 0, '2026-04-14 09:22:00', 'admin', '2026-04-14 09:22:00', 'admin')
ON DUPLICATE KEY UPDATE
    openid = VALUES(openid),
    nickname = VALUES(nickname),
    phone = VALUES(phone),
    avatar = VALUES(avatar),
    points = VALUES(points),
    member_level = VALUES(member_level),
    status = VALUES(status),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO orders (
    id, order_no, user_id, shop_name, pickup_type, contact_name, amount, status, status_text, note, order_type, pay_type, del_flag,
    create_time, create_by, update_time, update_by
) VALUES
    (4001, 'OD202604140001', 3001, '私人厨房', '到店自取', '用餐小王', 37.00, 'pendingPickup', '待取餐', '预计 12:35 可取餐', 'miniapp', 'wxpay', 0, '2026-04-14 12:18:00', 'admin', '2026-04-14 12:18:00', 'admin'),
    (4002, 'OD202604140002', 3001, '私人厨房', '到店自取', '用餐小王', 19.00, 'pendingPay', '待支付', '请在 15 分钟内完成支付', 'miniapp', 'cookNow', 0, '2026-04-14 11:42:00', 'admin', '2026-04-14 11:42:00', 'admin'),
    (4003, 'OD202604140003', 3002, '私人厨房', '到店自取', '爱吃川菜', 22.00, 'pendingComment', '待评价', '评价后可得 20 积分', 'miniapp', 'wxpay', 0, '2026-04-13 18:42:00', 'admin', '2026-04-13 18:42:00', 'admin'),
    (4004, 'OD202604140004', 3003, '私人厨房', '到店自取', '午餐搭子', 28.00, 'refund', '退款售后', '售后处理中', 'miniapp', 'wxpay', 0, '2026-04-10 19:03:00', 'admin', '2026-04-10 19:03:00', 'admin')
ON DUPLICATE KEY UPDATE
    order_no = VALUES(order_no),
    user_id = VALUES(user_id),
    shop_name = VALUES(shop_name),
    pickup_type = VALUES(pickup_type),
    contact_name = VALUES(contact_name),
    amount = VALUES(amount),
    status = VALUES(status),
    status_text = VALUES(status_text),
    note = VALUES(note),
    order_type = VALUES(order_type),
    pay_type = VALUES(pay_type),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO order_items (
    id, order_id, dish_id, dish_name, price, count, amount, del_flag,
    create_time, create_by, update_time, update_by
) VALUES
    (5001, 4001, 2001, '盐煎肉(不含白米饭)', 22.00, 1, 22.00, 0, '2026-04-14 12:18:00', 'admin', '2026-04-14 12:18:00', 'admin'),
    (5002, 4001, 2004, '番茄炒蛋', 15.00, 1, 15.00, 0, '2026-04-14 12:18:00', 'admin', '2026-04-14 12:18:00', 'admin'),
    (5003, 4002, 2002, '鱼香肉丝', 17.00, 1, 17.00, 0, '2026-04-14 11:42:00', 'admin', '2026-04-14 11:42:00', 'admin'),
    (5004, 4002, 2005, '白米饭', 2.00, 1, 2.00, 0, '2026-04-14 11:42:00', 'admin', '2026-04-14 11:42:00', 'admin'),
    (5005, 4003, 2003, '泡椒鸡杂', 17.00, 1, 17.00, 0, '2026-04-13 18:42:00', 'admin', '2026-04-13 18:42:00', 'admin'),
    (5006, 4003, 2006, '冰红茶', 5.00, 1, 5.00, 0, '2026-04-13 18:42:00', 'admin', '2026-04-13 18:42:00', 'admin'),
    (5007, 4004, 2004, '番茄炒蛋', 15.00, 1, 15.00, 0, '2026-04-10 19:03:00', 'admin', '2026-04-10 19:03:00', 'admin'),
    (5008, 4004, 2002, '清炒时蔬', 13.00, 1, 13.00, 0, '2026-04-10 19:03:00', 'admin', '2026-04-10 19:03:00', 'admin')
ON DUPLICATE KEY UPDATE
    order_id = VALUES(order_id),
    dish_id = VALUES(dish_id),
    dish_name = VALUES(dish_name),
    price = VALUES(price),
    count = VALUES(count),
    amount = VALUES(amount),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO coupons (
    id, name, type, discount_value, threshold, start_time, end_time, status, del_flag,
    create_time, create_by, update_time, update_by
) VALUES
    (6001, '满20减5', 'full_reduction', 5.00, 20.00, '2026-04-01 00:00:00', '2026-04-30 23:59:59', 1, 0, '2026-04-14 09:30:00', 'admin', '2026-04-14 09:30:00', 'admin'),
    (6002, '会员饮品券', 'single_item', 3.00, 10.00, '2026-04-01 00:00:00', '2026-05-31 23:59:59', 1, 0, '2026-04-14 09:31:00', 'admin', '2026-04-14 09:31:00', 'admin')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    type = VALUES(type),
    discount_value = VALUES(discount_value),
    threshold = VALUES(threshold),
    start_time = VALUES(start_time),
    end_time = VALUES(end_time),
    status = VALUES(status),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO user_coupons (
    id, user_id, coupon_id, status, used_at, expire_at, del_flag,
    create_time, create_by, update_time, update_by
) VALUES
    (7001, 3001, 6001, 'unused', NULL, '2026-04-30 23:59:59', 0, '2026-04-14 09:35:00', 'admin', '2026-04-14 09:35:00', 'admin'),
    (7002, 3001, 6002, 'unused', NULL, '2026-05-31 23:59:59', 0, '2026-04-14 09:36:00', 'admin', '2026-04-14 09:36:00', 'admin'),
    (7003, 3002, 6001, 'used', '2026-04-12 12:00:00', '2026-04-30 23:59:59', 0, '2026-04-14 09:37:00', 'admin', '2026-04-14 09:37:00', 'admin')
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id),
    coupon_id = VALUES(coupon_id),
    status = VALUES(status),
    used_at = VALUES(used_at),
    expire_at = VALUES(expire_at),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO shop_settings (
    id, shop_name, address, notice, business_hours, takeout_enabled, cook_now_enabled, wxpay_enabled, del_flag,
    create_time, create_by, update_time, update_by
) VALUES
    (8001, '私人厨房', '软件园 A 区 2 号楼', '点完即做菜，预计 15 分钟出餐。', '09:00 - 20:00', 1, 1, 1, 0, '2026-04-14 09:40:00', 'admin', '2026-04-14 09:40:00', 'admin')
ON DUPLICATE KEY UPDATE
    shop_name = VALUES(shop_name),
    address = VALUES(address),
    notice = VALUES(notice),
    business_hours = VALUES(business_hours),
    takeout_enabled = VALUES(takeout_enabled),
    cook_now_enabled = VALUES(cook_now_enabled),
    wxpay_enabled = VALUES(wxpay_enabled),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_user_theme_setting (
    id, user_code, nav_layout, topbar_color, sidebar_color, sidebar_collapsed,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000001, 'admin', 'side', 'linear-gradient(90deg, #34c06a 0%, #46b876 100%)', 'linear-gradient(180deg, #243244 0%, #26364b 100%)', 0,
     0, '2026-04-15 10:00:00', 'admin', '2026-04-15 10:00:00', 'admin')
ON DUPLICATE KEY UPDATE
    nav_layout = VALUES(nav_layout),
    topbar_color = VALUES(topbar_color),
    sidebar_color = VALUES(sidebar_color),
    sidebar_collapsed = VALUES(sidebar_collapsed),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_dashboard_card (
    id, card_key, title, card_value, extra_text, enabled, sort,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000011, 'todayOrderCount', '今日订单', '128', '较昨日 +12%', 1, 1, 0, '2026-04-15 10:10:00', 'admin', '2026-04-15 10:10:00', 'admin'),
    (2045000000000000012, 'pendingPickup', '待取餐订单', '16', '高峰时段需重点关注', 1, 2, 0, '2026-04-15 10:11:00', 'admin', '2026-04-15 10:11:00', 'admin'),
    (2045000000000000013, 'todayRevenue', '今日营业额', '¥3842', '现炒下单占比 34%', 1, 3, 0, '2026-04-15 10:12:00', 'admin', '2026-04-15 10:12:00', 'admin'),
    (2045000000000000014, 'newUsers', '会员新增', '23', '用户数据持续沉淀', 1, 4, 0, '2026-04-15 10:13:00', 'admin', '2026-04-15 10:13:00', 'admin')
ON DUPLICATE KEY UPDATE
    card_key = VALUES(card_key),
    title = VALUES(title),
    card_value = VALUES(card_value),
    extra_text = VALUES(extra_text),
    enabled = VALUES(enabled),
    sort = VALUES(sort),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_notice (
    id, title, content, status, sort, publish_time,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000021, '系统升级通知', '本周五 22:00 将进行系统升级，预计 20 分钟完成。', 1, 1, '2026-04-15 09:00:00', 0, '2026-04-15 09:00:00', 'admin', '2026-04-15 09:00:00', 'admin'),
    (2045000000000000022, '门店活动提醒', '五一活动页将在下周开放，请提前检查菜品和库存配置。', 1, 2, '2026-04-15 09:30:00', 0, '2026-04-15 09:30:00', 'admin', '2026-04-15 09:30:00', 'admin')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    content = VALUES(content),
    status = VALUES(status),
    sort = VALUES(sort),
    publish_time = VALUES(publish_time),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_menu (
    id, parent_id, name, title, path, component, icon, type, status, sort,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000031, 0, 'dashboard', '首页', '/dashboard', 'DashboardView', 'House', 'menu', 1, 1, 0, '2026-04-15 09:40:00', 'admin', '2026-04-15 09:40:00', 'admin'),
    (2045000000000000032, 0, 'categories', '分类管理', '/categories', 'CategoryView', 'Grid', 'menu', 1, 2, 0, '2026-04-15 09:41:00', 'admin', '2026-04-15 09:41:00', 'admin'),
    (2045000000000000033, 0, 'dishes', '菜品管理', '/dishes', 'DishView', 'List', 'menu', 1, 3, 0, '2026-04-15 09:42:00', 'admin', '2026-04-15 09:42:00', 'admin'),
    (2045000000000000034, 0, 'orders', '订单管理', '/orders', 'OrderView', 'Tickets', 'menu', 1, 4, 0, '2026-04-15 09:43:00', 'admin', '2026-04-15 09:43:00', 'admin'),
    (2045000000000000035, 0, 'users', '用户管理', '/users', 'UserView', 'User', 'menu', 1, 5, 0, '2026-04-15 09:44:00', 'admin', '2026-04-15 09:44:00', 'admin'),
    (2045000000000000036, 0, 'settings', '店铺设置', '/settings', 'SettingView', 'Setting', 'menu', 1, 6, 0, '2026-04-15 09:45:00', 'admin', '2026-04-15 09:45:00', 'admin'),
    (2045000000000000037, 0, 'system-group', '系统设置', '', '', 'FolderOpened', 'group', 1, 7, 0, '2026-04-15 09:46:00', 'admin', '2026-04-15 09:46:00', 'admin'),
    (2045000000000000038, 2045000000000000037, 'system-home-settings', '首页设置', '/system/home-settings', 'SystemHomeView', 'DataAnalysis', 'menu', 1, 1, 0, '2026-04-15 09:47:00', 'admin', '2026-04-15 09:47:00', 'admin'),
    (2045000000000000039, 2045000000000000037, 'system-notices', '公告管理', '/system/notices', 'SystemNoticeView', 'Notification', 'menu', 1, 2, 0, '2026-04-15 09:48:00', 'admin', '2026-04-15 09:48:00', 'admin'),
    (2045000000000000040, 2045000000000000037, 'system-menus', '菜单设置', '/system/menus', 'SystemMenuView', 'Menu', 'menu', 1, 3, 0, '2026-04-15 09:49:00', 'admin', '2026-04-15 09:49:00', 'admin')
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    title = VALUES(title),
    path = VALUES(path),
    component = VALUES(component),
    icon = VALUES(icon),
    type = VALUES(type),
    status = VALUES(status),
    sort = VALUES(sort),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_menu (
    id, parent_id, name, title, path, component, icon, type, status, sort,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000041, 2045000000000000037, 'system-personalization', '个性化', '/system/personalization', 'SystemPersonalizationView', 'Setting', 'menu', 1, 4, 0, '2026-04-15 09:50:00', 'admin', '2026-04-15 09:50:00', 'admin'),
    (2045000000000000042, 2045000000000000037, 'system-admin-users', '后台用户', '/system/admin-users', 'SystemAdminUserView', 'User', 'menu', 1, 5, 0, '2026-04-15 09:51:00', 'admin', '2026-04-15 09:51:00', 'admin')
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    name = VALUES(name),
    title = VALUES(title),
    path = VALUES(path),
    component = VALUES(component),
    icon = VALUES(icon),
    type = VALUES(type),
    status = VALUES(status),
    sort = VALUES(sort),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_personalization (
    id, logo_url, system_name, system_intro, welcome_text, system_background_image, login_background_image, theme_color,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000051, '', 'Kitchen Admin', '私人厨房后台管理', '欢迎使用私人厨房后台管理系统', '', '', '#409EFF',
     0, '2026-04-15 10:20:00', 'admin', '2026-04-15 10:20:00', 'admin')
ON DUPLICATE KEY UPDATE
    logo_url = VALUES(logo_url),
    system_name = VALUES(system_name),
    system_intro = VALUES(system_intro),
    welcome_text = VALUES(welcome_text),
    system_background_image = VALUES(system_background_image),
    login_background_image = VALUES(login_background_image),
    theme_color = VALUES(theme_color),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_user (
    id, username, password, display_name, phone, avatar, role_name, status, remark,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000061, 'admin', 'admin123', '管理员', '13800138000', '', '超级管理员', 1, '系统默认后台管理员账号',
     0, '2026-04-15 10:30:00', 'admin', '2026-04-15 10:30:00', 'admin')
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    display_name = VALUES(display_name),
    phone = VALUES(phone),
    avatar = VALUES(avatar),
    role_name = VALUES(role_name),
    status = VALUES(status),
    remark = VALUES(remark),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);
