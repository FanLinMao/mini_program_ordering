ALTER TABLE users ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';
ALTER TABLE categories ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';
ALTER TABLE dishes ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';
ALTER TABLE orders ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';
ALTER TABLE order_items ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';
ALTER TABLE coupons ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';
ALTER TABLE user_coupons ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';
ALTER TABLE shop_settings ADD COLUMN del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag';

CREATE TABLE IF NOT EXISTS sys_user_theme_setting (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    user_code VARCHAR(64) NOT NULL COMMENT 'Current user code',
    nav_layout VARCHAR(32) NOT NULL DEFAULT 'side' COMMENT 'Navigation layout mode',
    topbar_color VARCHAR(128) NOT NULL DEFAULT 'linear-gradient(90deg, #34c06a 0%, #46b876 100%)' COMMENT 'Top navigation palette',
    sidebar_color VARCHAR(128) NOT NULL DEFAULT 'linear-gradient(180deg, #243244 0%, #26364b 100%)' COMMENT 'Sidebar palette',
    sidebar_collapsed TINYINT NOT NULL DEFAULT 0 COMMENT 'Sidebar collapsed flag',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by',
    UNIQUE KEY uk_sys_user_theme_setting_user_code (user_code)
);

ALTER TABLE sys_user_theme_setting
    MODIFY COLUMN nav_layout VARCHAR(32) NOT NULL DEFAULT 'side' COMMENT 'Navigation layout mode',
    MODIFY COLUMN topbar_color VARCHAR(128) NOT NULL DEFAULT 'linear-gradient(90deg, #34c06a 0%, #46b876 100%)' COMMENT 'Top navigation palette',
    MODIFY COLUMN sidebar_color VARCHAR(128) NOT NULL DEFAULT 'linear-gradient(180deg, #243244 0%, #26364b 100%)' COMMENT 'Sidebar palette';

INSERT INTO sys_user_theme_setting (
    id, user_code, nav_layout, topbar_color, sidebar_color, sidebar_collapsed,
    del_flag, create_time, create_by, update_time, update_by
) VALUES (
    2045000000000000001, 'admin', 'side', 'linear-gradient(90deg, #34c06a 0%, #46b876 100%)', 'linear-gradient(180deg, #243244 0%, #26364b 100%)', 0,
    0, NOW(), 'admin', NOW(), 'admin'
)
ON DUPLICATE KEY UPDATE
    nav_layout = VALUES(nav_layout),
    topbar_color = VALUES(topbar_color),
    sidebar_color = VALUES(sidebar_color),
    sidebar_collapsed = VALUES(sidebar_collapsed),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

UPDATE sys_user_theme_setting
SET
    nav_layout = CASE
        WHEN nav_layout = 'classic' THEN 'side'
        WHEN nav_layout = 'compact' THEN 'mix'
        ELSE nav_layout
    END,
    topbar_color = CASE
        WHEN topbar_color = 'fresh-green' THEN 'linear-gradient(90deg, #34c06a 0%, #46b876 100%)'
        WHEN topbar_color = 'sky-blue' THEN 'linear-gradient(90deg, #4d81f7 0%, #5c92ff 100%)'
        WHEN topbar_color = 'vital-orange' THEN 'linear-gradient(90deg, #f59e0b 0%, #f97316 100%)'
        WHEN topbar_color = 'night-black' THEN 'linear-gradient(90deg, #111827 0%, #334155 100%)'
        ELSE topbar_color
    END,
    sidebar_color = CASE
        WHEN sidebar_color = 'night-blue' THEN 'linear-gradient(180deg, #243244 0%, #26364b 100%)'
        WHEN sidebar_color = 'forest-green' THEN 'linear-gradient(180deg, #163b2c 0%, #22543d 100%)'
        WHEN sidebar_color = 'coffee-brown' THEN 'linear-gradient(180deg, #3b2f1f 0%, #5a472e 100%)'
        WHEN sidebar_color = 'midnight-blue' THEN 'linear-gradient(180deg, #1f2937 0%, #111827 100%)'
        ELSE sidebar_color
    END,
    update_time = NOW(),
    update_by = 'admin';

CREATE TABLE IF NOT EXISTS sys_dashboard_card (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    card_key VARCHAR(64) NOT NULL COMMENT 'Card key',
    title VARCHAR(64) NOT NULL COMMENT 'Card title',
    card_value VARCHAR(64) NOT NULL COMMENT 'Card value',
    extra_text VARCHAR(255) DEFAULT NULL COMMENT 'Extra text',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT 'Enabled flag',
    sort INT NOT NULL DEFAULT 0 COMMENT 'Sort value',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
);

CREATE TABLE IF NOT EXISTS sys_notice (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    title VARCHAR(128) NOT NULL COMMENT 'Notice title',
    content VARCHAR(1000) DEFAULT NULL COMMENT 'Notice content',
    status TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 1 published, 0 draft',
    sort INT NOT NULL DEFAULT 0 COMMENT 'Sort value',
    publish_time DATETIME DEFAULT NULL COMMENT 'Publish time',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
);

CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    parent_id BIGINT NOT NULL DEFAULT 0 COMMENT 'Parent menu id',
    name VARCHAR(64) NOT NULL COMMENT 'Route name',
    title VARCHAR(64) NOT NULL COMMENT 'Menu title',
    path VARCHAR(128) DEFAULT NULL COMMENT 'Route path',
    component VARCHAR(128) DEFAULT NULL COMMENT 'Component path',
    icon VARCHAR(64) DEFAULT NULL COMMENT 'Icon name',
    type VARCHAR(32) NOT NULL DEFAULT 'menu' COMMENT 'Menu type',
    status TINYINT NOT NULL DEFAULT 1 COMMENT 'Enabled flag',
    sort INT NOT NULL DEFAULT 0 COMMENT 'Sort value',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
);

INSERT INTO sys_dashboard_card (
    id, card_key, title, card_value, extra_text, enabled, sort,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000011, 'todayOrderCount', '今日订单', '128', '较昨日 +12%', 1, 1, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000012, 'pendingPickup', '待取餐订单', '16', '高峰时段需重点关注', 1, 2, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000013, 'todayRevenue', '今日营业额', '¥3842', '现炒下单占比 34%', 1, 3, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000014, 'newUsers', '会员新增', '23', '用户数据持续沉淀', 1, 4, 0, NOW(), 'admin', NOW(), 'admin')
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
    (2045000000000000021, '系统升级通知', '本周五 22:00 将进行系统升级，预计 20 分钟完成。', 1, 1, NOW(), 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000022, '门店活动提醒', '五一活动页将在下周开放，请提前检查菜品和库存配置。', 1, 2, NOW(), 0, NOW(), 'admin', NOW(), 'admin')
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
    (2045000000000000031, 0, 'dashboard', '首页', '/dashboard', 'DashboardView', 'House', 'menu', 1, 1, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000032, 0, 'categories', '分类管理', '/categories', 'CategoryView', 'Grid', 'menu', 1, 2, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000033, 0, 'dishes', '菜品管理', '/dishes', 'DishView', 'List', 'menu', 1, 3, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000034, 0, 'orders', '订单管理', '/orders', 'OrderView', 'Tickets', 'menu', 1, 4, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000035, 0, 'users', '用户管理', '/users', 'UserView', 'User', 'menu', 1, 5, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000036, 0, 'settings', '店铺设置', '/settings', 'SettingView', 'Setting', 'menu', 1, 6, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000037, 0, 'system-group', '系统设置', '', '', 'FolderOpened', 'group', 1, 7, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000038, 2045000000000000037, 'system-home-settings', '首页设置', '/system/home-settings', 'SystemHomeView', 'DataAnalysis', 'menu', 1, 1, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000039, 2045000000000000037, 'system-notices', '公告管理', '/system/notices', 'SystemNoticeView', 'Notification', 'menu', 1, 2, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000040, 2045000000000000037, 'system-menus', '菜单设置', '/system/menus', 'SystemMenuView', 'Menu', 'menu', 1, 3, 0, NOW(), 'admin', NOW(), 'admin')
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

CREATE TABLE IF NOT EXISTS sys_personalization (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    logo_url VARCHAR(255) DEFAULT NULL COMMENT 'System logo url',
    system_name VARCHAR(128) NOT NULL COMMENT 'System name',
    system_intro VARCHAR(255) DEFAULT NULL COMMENT 'System introduction',
    welcome_text VARCHAR(255) DEFAULT NULL COMMENT 'Welcome text',
    system_background_image VARCHAR(255) DEFAULT NULL COMMENT 'System background image',
    login_background_image VARCHAR(255) DEFAULT NULL COMMENT 'Login background image',
    theme_color VARCHAR(32) DEFAULT '#409EFF' COMMENT 'Theme color',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
);

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    username VARCHAR(64) NOT NULL COMMENT 'Login username',
    password VARCHAR(128) NOT NULL COMMENT 'Login password',
    display_name VARCHAR(64) NOT NULL COMMENT 'Display name',
    phone VARCHAR(20) DEFAULT NULL COMMENT 'Phone number',
    avatar VARCHAR(255) DEFAULT NULL COMMENT 'Avatar url',
    role_name VARCHAR(64) DEFAULT '超级管理员' COMMENT 'Role name',
    status TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 1 enabled, 0 disabled',
    remark VARCHAR(255) DEFAULT NULL COMMENT 'Remark',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by',
    UNIQUE KEY uk_sys_user_username (username)
);

CREATE TABLE IF NOT EXISTS sys_user_login_log (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    username VARCHAR(64) NOT NULL COMMENT 'Login username',
    display_name VARCHAR(64) DEFAULT NULL COMMENT 'Display name',
    action_type VARCHAR(32) NOT NULL COMMENT 'Action type',
    success_flag TINYINT NOT NULL DEFAULT 1 COMMENT 'Success flag',
    ip_address VARCHAR(64) DEFAULT NULL COMMENT 'Client IP',
    user_agent VARCHAR(255) DEFAULT NULL COMMENT 'User agent',
    remark VARCHAR(255) DEFAULT NULL COMMENT 'Remark',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
);

INSERT INTO sys_user_login_log (
    id, username, display_name, action_type, success_flag, ip_address, user_agent, remark,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000062, 'admin', '管理员', 'LOGIN', 1, '127.0.0.1', 'Chrome', '登录成功',
     0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000063, 'admin', '管理员', 'UPDATE_PROFILE', 1, '127.0.0.1', 'Chrome', '更新个人资料',
     0, NOW(), 'admin', NOW(), 'admin')
ON DUPLICATE KEY UPDATE
    display_name = VALUES(display_name),
    action_type = VALUES(action_type),
    success_flag = VALUES(success_flag),
    ip_address = VALUES(ip_address),
    user_agent = VALUES(user_agent),
    remark = VALUES(remark),
    del_flag = VALUES(del_flag),
    update_time = VALUES(update_time),
    update_by = VALUES(update_by);

INSERT INTO sys_menu (
    id, parent_id, name, title, path, component, icon, type, status, sort,
    del_flag, create_time, create_by, update_time, update_by
) VALUES
    (2045000000000000041, 2045000000000000037, 'system-personalization', '个性化', '/system/personalization', 'SystemPersonalizationView', 'Setting', 'menu', 1, 4, 0, NOW(), 'admin', NOW(), 'admin'),
    (2045000000000000042, 2045000000000000037, 'system-admin-users', '后台用户', '/system/admin-users', 'SystemAdminUserView', 'User', 'menu', 1, 5, 0, NOW(), 'admin', NOW(), 'admin')
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
     0, NOW(), 'admin', NOW(), 'admin')
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
     0, NOW(), 'admin', NOW(), 'admin')
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
