# 项目设计说明

## 前台现状梳理

当前小程序前台已经完成了一个可运行的点餐闭环，核心能力包括：

- 点餐首页
- 购物车弹层
- 确认订单
- 微信支付/现炒下单
- 我的页面
- 我的订单列表
- 订单详情

### 当前前台主要模块

#### 1. 菜品与分类

来源文件：

- `mock/menu.js`

当前已有字段：

- 店铺信息 `shopInfo`
- 菜品分类 `dishCategories`
- 菜品字段：
  - `id`
  - `name`
  - `price`
  - `monthlySales`
  - `praise`
  - `extra`
  - `image`

#### 2. 购物车

来源文件：

- `composables/useCart.js`

当前能力：

- 加菜
- 减菜
- 清空购物车
- 统计商品件数
- 统计总金额
- 控制是否可下单

#### 3. 订单

来源文件：

- `mock/orders.js`

当前订单字段：

- `id`
- `title`
- `time`
- `amount`
- `status`
- `statusText`
- `note`
- `shopName`
- `pickupType`
- `contactName`
- `items`

订单商品明细字段：

- `id`
- `name`
- `count`
- `price`

#### 4. 下单方式

来源文件：

- `pages/order/confirm.vue`

当前支持两种方式：

- 微信支付
- 现炒下单

其中：

- 微信支付走 `services/payment.js`
- 现炒下单走 `services/order.js`

#### 5. 订单页面

当前已有页面：

- `pages/order/confirm.vue`
- `pages/order/list.vue`
- `pages/order/detail.vue`

支持的订单状态：

- 全部订单
- 待支付
- 待取餐
- 待评价
- 退款售后

## 后台管理系统目标

后台系统要能够承接当前前台已有的数据模型和业务流程，并让前台从 mock 数据平滑切换到真实接口。

后台管理系统建议分为以下 5 个核心模块：

- 菜品管理
- 订单管理
- 用户管理
- 优惠券/权益管理
- 店铺/运营配置

## 后台模块设计

### 1. 菜品管理

目标：

- 维护前台菜品与分类
- 控制上下架
- 调整价格和排序

后台需支持：

- 分类增删改查
- 菜品增删改查
- 菜品图片上传
- 菜品上下架
- 菜品排序
- 推荐文案维护

对应前台字段：

- 分类名
- 菜名
- 单价
- 月售
- 推荐描述
- 标签文案
- 图片

建议页面：

- 分类管理页
- 菜品列表页
- 菜品编辑页

### 2. 订单管理

目标：

- 处理前台下单后的所有订单
- 管理订单状态流转

后台需支持：

- 查看全部订单
- 按状态筛选订单
- 查看订单详情
- 修改订单状态
- 处理退款售后

建议订单状态：

- `pendingPay` 待支付
- `confirmed` 已接单
- `cooking` 制作中
- `pickup` 待取餐
- `completed` 已完成
- `review` 待评价
- `refund` 退款售后
- `cancelled` 已取消

当前前台已用到的状态：

- 待支付
- 待取餐
- 待评价
- 退款售后

建议后台订单操作：

- 确认接单
- 改为制作中
- 改为待取餐
- 改为已完成
- 取消订单
- 处理退款

建议页面：

- 订单列表页
- 订单详情页

### 3. 用户管理

目标：

- 管理下单用户
- 为“我的”页面提供用户中心数据

后台需支持：

- 用户列表
- 查看用户详情
- 用户状态管理
- 用户订单统计
- 用户积分统计
- 用户优惠券统计

对应前台能力：

- 我的订单
- 会员积分
- 优惠券数量
- 用户昵称

### 4. 优惠券/权益管理

目标：

- 支撑“我的权益”
- 后续支持营销活动

后台需支持：

- 优惠券模板管理
- 发券
- 券状态管理
- 积分规则
- 会员等级规则

对应前台字段：

- 会员积分
- 可用优惠券
- 会员等级
- 本月节省金额

### 5. 店铺/运营配置

目标：

- 统一配置店铺信息和前台展示内容

后台需支持：

- 店铺名称
- 门店地址
- 营业时间
- 公告
- 下单提示文案
- 下单方式开关
- 微信支付配置状态

## 后台第一阶段建议范围

为了尽快落地，后台第一版建议只做最核心的 3 个页面：

### 1. 后台首页

展示：

- 今日订单数
- 待支付订单数
- 待取餐订单数
- 今日营业额

### 2. 菜品管理页

展示：

- 分类列表
- 菜品列表
- 菜品状态
- 价格

操作：

- 新增菜品
- 编辑菜品
- 上下架
- 删除

### 3. 订单管理页

展示：

- 订单列表
- 状态筛选
- 订单详情抽屉或弹窗

操作：

- 确认接单
- 改状态
- 处理退款

## 数据库表建议

建议至少包含以下数据表：

### 1. `users`

字段建议：

- `id`
- `openid`
- `nickname`
- `phone`
- `avatar`
- `points`
- `member_level`
- `status`
- `created_at`

### 2. `categories`

字段建议：

- `id`
- `name`
- `sort`
- `status`
- `created_at`

### 3. `dishes`

字段建议：

- `id`
- `category_id`
- `name`
- `price`
- `image`
- `monthly_sales`
- `praise`
- `extra`
- `status`
- `sort`
- `created_at`

### 4. `orders`

字段建议：

- `id`
- `user_id`
- `order_no`
- `shop_name`
- `pickup_type`
- `contact_name`
- `amount`
- `status`
- `status_text`
- `note`
- `order_type`
- `pay_type`
- `created_at`
- `updated_at`

### 5. `order_items`

字段建议：

- `id`
- `order_id`
- `dish_id`
- `dish_name`
- `price`
- `count`
- `amount`

### 6. `coupons`

字段建议：

- `id`
- `name`
- `type`
- `discount_value`
- `threshold`
- `start_time`
- `end_time`
- `status`

### 7. `user_coupons`

字段建议：

- `id`
- `user_id`
- `coupon_id`
- `status`
- `used_at`
- `expire_at`

### 8. `shop_settings`

字段建议：

- `id`
- `shop_name`
- `address`
- `notice`
- `business_hours`
- `takeout_enabled`
- `cook_now_enabled`
- `wxpay_enabled`

## 前后台接口建议

### 菜单接口

`GET /api/menu`

用途：

- 返回店铺信息
- 返回分类
- 返回菜品

### 创建订单接口

`POST /api/orders`

用途：

- 现炒下单
- 写入订单主表和订单明细表
- 初始状态写成 `pendingPay`

### 微信预支付接口

`POST /api/payments/prepay`

用途：

- 返回微信支付所需参数
- 给前台 `uni.requestPayment` 调用

返回关键字段：

- `timeStamp`
- `nonceStr`
- `package`
- `signType`
- `paySign`

### 我的订单列表接口

`GET /api/orders/my?status=pendingPay`

用途：

- 获取用户自己的订单列表
- 支持按状态筛选

### 订单详情接口

`GET /api/orders/:id`

用途：

- 获取订单详情

### 后台订单管理接口

- `GET /admin/orders`
- `GET /admin/orders/:id`
- `PATCH /admin/orders/:id/status`

### 后台菜品管理接口

- `GET /admin/dishes`
- `POST /admin/dishes`
- `PATCH /admin/dishes/:id`
- `PATCH /admin/dishes/:id/status`

## 订单状态流转建议

建议订单状态流转如下：

1. 用户现炒下单
   状态：`pendingPay`

2. 用户支付成功
   状态：`confirmed`

3. 商家开始制作
   状态：`cooking`

4. 菜品制作完成
   状态：`pickup`

5. 用户完成取餐
   状态：`completed`

6. 完成后等待评价
   状态：`review`

7. 售后介入
   状态：`refund`

## 后台技术栈建议

如果下一步要正式开发后台，推荐如下：

### 前端后台

- Vue 3
- Element Plus

### 后端

- Java 17+
- Spring Boot
- MyBatis-Plus

推荐采用标准分层结构设计：

- `controller`
  负责接口入口、参数接收、结果返回
- `service`
  定义业务接口
- `service/impl`
  编写业务实现逻辑
- `dao` 或 `mapper`
  负责数据库访问
- `entity`
  对应数据库实体对象
- `dto`
  请求参数对象
- `vo`
  响应结果对象
- `config`
  项目配置类
- `common`
  通用返回体、异常、枚举、工具类

### 数据库

- MySQL

### 文件存储

- 本地存储或 OSS/COS

## 下一步建议

建议按下面顺序推进：

1. 先确定后台技术栈
2. 确定数据库表结构
3. 先做后台原型页面
4. 再逐步把前台 mock 替换成真实接口

优先级建议：

- 第一优先：订单管理
- 第二优先：菜品管理
- 第三优先：店铺配置
- 第四优先：用户和权益管理
