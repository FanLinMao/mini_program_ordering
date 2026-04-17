CREATE DATABASE IF NOT EXISTS private_kitchen DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE private_kitchen;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    openid VARCHAR(64) NOT NULL COMMENT 'WeChat openid',
    nickname VARCHAR(64) NOT NULL COMMENT 'User nickname',
    phone VARCHAR(20) DEFAULT NULL COMMENT 'Mobile phone',
    avatar VARCHAR(255) DEFAULT NULL COMMENT 'Avatar',
    points INT DEFAULT 0 COMMENT 'Points',
    member_level VARCHAR(32) DEFAULT '普通' COMMENT 'Member level',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1 enabled, 0 disabled',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
) COMMENT='User table';

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    name VARCHAR(64) NOT NULL COMMENT 'Category name',
    sort INT DEFAULT 0 COMMENT 'Sort number',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1 enabled, 0 disabled',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
) COMMENT='Dish category table';

CREATE TABLE IF NOT EXISTS dishes (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    category_id BIGINT NOT NULL COMMENT 'Category id',
    name VARCHAR(128) NOT NULL COMMENT 'Dish name',
    price DECIMAL(10, 2) NOT NULL COMMENT 'Sale price',
    image VARCHAR(255) DEFAULT NULL COMMENT 'Image url',
    monthly_sales INT DEFAULT 0 COMMENT 'Monthly sales',
    praise INT DEFAULT 0 COMMENT 'Praise count',
    extra VARCHAR(255) DEFAULT NULL COMMENT 'Extra description',
    sort INT DEFAULT 0 COMMENT 'Sort number',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1 on shelf, 0 off shelf',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
) COMMENT='Dish table';

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    order_no VARCHAR(64) NOT NULL COMMENT 'Order number',
    user_id BIGINT NOT NULL COMMENT 'User id',
    shop_name VARCHAR(128) DEFAULT NULL COMMENT 'Shop name',
    pickup_type VARCHAR(32) DEFAULT NULL COMMENT 'Pickup type',
    contact_name VARCHAR(64) DEFAULT NULL COMMENT 'Contact name',
    amount DECIMAL(10, 2) NOT NULL COMMENT 'Order amount',
    status VARCHAR(32) NOT NULL COMMENT 'Order status code',
    status_text VARCHAR(32) NOT NULL COMMENT 'Order status text',
    note VARCHAR(255) DEFAULT NULL COMMENT 'Order note',
    order_type VARCHAR(32) DEFAULT NULL COMMENT 'Order type',
    pay_type VARCHAR(32) DEFAULT NULL COMMENT 'Pay type',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
) COMMENT='Order table';

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    order_id BIGINT NOT NULL COMMENT 'Order id',
    dish_id BIGINT DEFAULT NULL COMMENT 'Dish id',
    dish_name VARCHAR(128) NOT NULL COMMENT 'Dish name',
    price DECIMAL(10, 2) NOT NULL COMMENT 'Unit price',
    count INT NOT NULL COMMENT 'Count',
    amount DECIMAL(10, 2) NOT NULL COMMENT 'Item amount',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
    ,KEY idx_order_items_order_id (order_id)
    ,KEY idx_order_items_dish_id (dish_id)
) COMMENT='Order item table';

CREATE TABLE IF NOT EXISTS coupons (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    name VARCHAR(128) NOT NULL COMMENT 'Coupon name',
    type VARCHAR(32) NOT NULL COMMENT 'Coupon type',
    discount_value DECIMAL(10, 2) NOT NULL COMMENT 'Discount value',
    threshold DECIMAL(10, 2) DEFAULT 0 COMMENT 'Threshold amount',
    start_time DATETIME DEFAULT NULL COMMENT 'Start time',
    end_time DATETIME DEFAULT NULL COMMENT 'End time',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1 enabled, 0 disabled',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
) COMMENT='Coupon table';

CREATE TABLE IF NOT EXISTS user_coupons (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    user_id BIGINT NOT NULL COMMENT 'User id',
    coupon_id BIGINT NOT NULL COMMENT 'Coupon id',
    status VARCHAR(32) DEFAULT 'unused' COMMENT 'Coupon status',
    used_at DATETIME DEFAULT NULL COMMENT 'Used time',
    expire_at DATETIME DEFAULT NULL COMMENT 'Expire time',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
) COMMENT='User coupon table';

CREATE TABLE IF NOT EXISTS user_addresses (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    user_id BIGINT NOT NULL COMMENT 'User id',
    contact_name VARCHAR(64) NOT NULL COMMENT 'Contact name',
    phone VARCHAR(20) NOT NULL COMMENT 'Phone',
    province VARCHAR(64) DEFAULT NULL COMMENT 'Province',
    city VARCHAR(64) DEFAULT NULL COMMENT 'City',
    district VARCHAR(64) DEFAULT NULL COMMENT 'District',
    detail_address VARCHAR(255) NOT NULL COMMENT 'Detail address',
    tag VARCHAR(32) DEFAULT '家' COMMENT 'Address tag',
    is_default TINYINT NOT NULL DEFAULT 0 COMMENT 'Default address flag',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by',
    KEY idx_user_addresses_user_id (user_id),
    KEY idx_user_addresses_default (user_id, is_default)
) COMMENT='User address table';

CREATE TABLE IF NOT EXISTS shop_settings (
    id BIGINT PRIMARY KEY COMMENT 'Primary key',
    shop_name VARCHAR(128) NOT NULL COMMENT 'Shop name',
    address VARCHAR(255) DEFAULT NULL COMMENT 'Address',
    notice VARCHAR(255) DEFAULT NULL COMMENT 'Notice',
    business_hours VARCHAR(64) DEFAULT NULL COMMENT 'Business hours',
    takeout_enabled TINYINT DEFAULT 1 COMMENT 'Takeout enabled',
    cook_now_enabled TINYINT DEFAULT 1 COMMENT 'Cook now enabled',
    wxpay_enabled TINYINT DEFAULT 1 COMMENT 'WeChat pay enabled',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete flag',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT 'Create by',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    update_by VARCHAR(64) DEFAULT 'system' COMMENT 'Update by'
) COMMENT='Shop setting table';

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
) COMMENT='System user theme setting';

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
) COMMENT='System dashboard card';

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
) COMMENT='System notice';

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
) COMMENT='System menu';

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
) COMMENT='System personalization setting';

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
) COMMENT='System user';

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
) COMMENT='System user login log';
