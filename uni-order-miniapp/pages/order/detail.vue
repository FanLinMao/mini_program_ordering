<template>
  <view class="detail-page">
    <view class="status-hero">
      <text class="status-title">{{ statusTitle }}</text>
      <text class="status-subtitle">{{ statusSubtitle }}</text>
    </view>

    <view v-if="order" class="card">
      <view class="card-header">
        <text class="card-title">订单信息</text>
      </view>
      <view class="info-row">
        <text class="info-label">订单号</text>
        <text class="info-value">{{ order.id }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">下单时间</text>
        <text class="info-value">{{ order.time }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">门店信息</text>
        <text class="info-value">{{ order.shopName }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">取餐方式</text>
        <text class="info-value">{{ order.pickupType }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">联系人</text>
        <text class="info-value">{{ order.contactName }}</text>
      </view>
    </view>

    <view v-if="order" class="card">
      <view class="card-header">
        <text class="card-title">商品明细</text>
      </view>
      <view v-for="item in order.items" :key="item.id" class="goods-row">
        <view>
          <text class="goods-name">{{ item.name }}</text>
          <text class="goods-count">x{{ item.count }}</text>
        </view>
        <text class="goods-price">¥{{ (item.price * item.count).toFixed(2) }}</text>
      </view>
      <view class="total-row">
        <text class="total-label">合计金额</text>
        <text class="total-value">¥{{ order.amount }}</text>
      </view>
    </view>

    <view v-if="order && order.status === 'pendingPay'" class="action-bar safe-bottom">
      <button class="pay-button" @tap="payOrderNow">立即支付</button>
    </view>

    <view v-if="!order" class="empty-card">
      <text class="empty-title">未找到订单</text>
      <text class="empty-text">请返回订单列表重新选择查看。</text>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getOrderById } from '@/mock/orders'

const orderId = ref('')

onLoad((options) => {
  orderId.value = options.id || ''
})

const order = computed(() => getOrderById(orderId.value))
const statusTitleMap = {
  pendingPay: '订单待支付',
  pickup: '订单待取餐',
  review: '订单待评价',
  refund: '订单退款售后中'
}

const statusTitle = computed(() => {
  if (!order.value) {
    return '订单详情'
  }

  return statusTitleMap[order.value.status] || '订单详情'
})

const statusSubtitle = computed(() => {
  if (!order.value) {
    return '请返回订单列表重新选择。'
  }

  return order.value.note || '感谢您的信任，期待再次光临。'
})

function payOrderNow() {
  if (!order.value) return

  uni.showToast({
    title: `立即支付：${order.value.id}`,
    icon: 'none'
  })
}
</script>

<style lang="scss" scoped>
.detail-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #fff7df 0%, #f7f4ee 260rpx);
  padding: 24rpx 24rpx 40rpx;
}

.status-hero,
.card,
.empty-card {
  background: #fffdf8;
  border-radius: 28rpx;
}

.status-hero {
  padding: 30rpx 26rpx;
}

.status-title {
  display: block;
  color: #2f2418;
  font-size: 38rpx;
  font-weight: 800;
}

.status-subtitle {
  display: block;
  margin-top: 10rpx;
  color: #9f907f;
  font-size: 24rpx;
}

.card {
  margin-top: 20rpx;
  padding: 24rpx;
}

.card-header {
  margin-bottom: 10rpx;
}

.card-title {
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.info-row,
.goods-row,
.total-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 18rpx 0;
  border-top: 2rpx solid #f2ebdf;
}

.info-row:first-of-type,
.goods-row:first-of-type {
  border-top: 0;
}

.info-label,
.info-value,
.goods-name,
.goods-count,
.goods-price,
.total-label,
.total-value {
  display: block;
}

.info-label,
.goods-name,
.total-label {
  color: #2f2418;
}

.info-label,
.goods-count,
.info-value {
  font-size: 24rpx;
}

.info-value,
.goods-count {
  color: #8f806f;
  text-align: right;
}

.goods-name {
  font-size: 28rpx;
  font-weight: 700;
}

.goods-count {
  margin-top: 8rpx;
}

.goods-price,
.total-value {
  color: #ff5d21;
  font-size: 30rpx;
  font-weight: 800;
}

.total-row {
  margin-top: 8rpx;
}

.total-label {
  font-size: 28rpx;
  font-weight: 700;
}

.empty-card {
  margin-top: 24rpx;
  padding: 48rpx 30rpx;
  text-align: center;
}

.action-bar {
  position: fixed;
  left: 24rpx;
  right: 24rpx;
  bottom: 24rpx;
  padding: 20rpx 24rpx;
  border-radius: 28rpx;
  background: rgba(255, 253, 248, 0.96);
}

.pay-button {
  width: 100%;
  height: 84rpx;
  line-height: 84rpx;
  border-radius: 42rpx;
  background: linear-gradient(135deg, #ffde56 0%, #ffc818 100%);
  color: #5a3c00;
  font-size: 30rpx;
  font-weight: 700;
}

.pay-button::after {
  border: 0;
}

.empty-title {
  display: block;
  color: #2f2418;
  font-size: 32rpx;
  font-weight: 700;
}

.empty-text {
  display: block;
  margin-top: 12rpx;
  color: #9f907f;
  font-size: 24rpx;
}
</style>
