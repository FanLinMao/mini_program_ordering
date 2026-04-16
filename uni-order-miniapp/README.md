# uni-order-miniapp

一个可直接在 HBuilderX 中打开运行的 `uni-app + Vue3` 点餐小程序演示项目。

## 已完成

- 参考截图实现点餐首页布局
- 左侧分类 / 右侧菜品 / 底部购物车结算栏
- 订单确认页
- `uni-ui` 常用组件本地化引入：`uni-icons`、`uni-badge`、`uni-popup`、`uni-number-box`
- 微信支付调用入口封装在 `services/payment.js`

## 运行方式

### 在 HBuilderX 中运行

1. 用 HBuilderX 打开目录 `D:/CodexProjects/uni-order-miniapp`
2. 直接点击“运行” -> “运行到小程序模拟器” -> “微信开发者工具”
3. HBuilderX 会自动编译，并把编译产物输出到 `unpackage/dist/dev/mp-weixin`

### 在微信开发者工具中打开

不要直接打开源码根目录 `D:/CodexProjects/uni-order-miniapp`，因为 `uni-app` 源码项目根目录没有 `app.json`。

如果你想手动在微信开发者工具中打开项目，请打开编译后的目录：

- `D:/CodexProjects/uni-order-miniapp/unpackage/dist/dev/mp-weixin`

前提是你至少先在 HBuilderX 里成功运行过一次“运行到微信小程序”，这样 `unpackage/dist/dev/mp-weixin` 才会生成。

## 微信支付接入说明

当前项目为了方便你先看界面效果，支付逻辑默认走模拟成功流程：

- 文件：`services/payment.js`
- 方法：`payOrder(orderPayload, { mock: true })`

你正式接入时改这两处：

1. 把 `createMockPrepay` 替换成你的后端预支付接口请求，后端返回 `timeStamp / nonceStr / package / signType / paySign`
2. 在 `pages/order/confirm.vue` 中把 `mock: true` 改成 `mock: false`

## 需要你替换的配置

- `manifest.json` 中的微信小程序 `appid`
- 支付后台接口地址、商户号和签名逻辑

## 说明

因为微信支付必须依赖真实小程序主体、商户配置和服务端签名，所以我已经把前端调用链和页面流程接好，但真实扣款前还需要你自己的后端参数。
