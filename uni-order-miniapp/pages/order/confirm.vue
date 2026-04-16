<template>
  <view class="confirm-page safe-bottom">
    <view class="hero">
      <text class="hero-title">预计 15 分钟出餐</text>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">商品清单</text>
        <text class="section-tip">{{ totalCount }} 件商品</text>
      </view>

      <view v-for="item in items" :key="item.id" class="order-item">
        <image class="cover" :src="item.image" mode="aspectFill" />
        <view class="info">
          <text class="name">{{ item.name }}</text>
          <text class="meta">x{{ item.count }}</text>
        </view>
        <text class="price">¥{{ (item.price * item.count).toFixed(2) }}</text>
      </view>
    </view>

    <view class="section">
      <view class="pay-row">
        <text>餐品小计</text>
        <text>¥{{ totalPrice.toFixed(2) }}</text>
      </view>
      <view class="pay-row">
        <text>包装配送</text>
        <text>¥0.00</text>
      </view>
      <view class="pay-row total">
        <text>实付金额</text>
        <text>¥{{ totalPrice.toFixed(2) }}</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">下单方式</text>
        <text class="section-tip">微信支付 / 现炒下单</text>
      </view>

      <view
        :class="['pay-method', selectedMode === 'wxpay' ? 'active' : '']"
        @tap="selectedMode = 'wxpay'"
      >
        <view class="method-main">
          <view class="pay-icon">微</view>
          <view>
            <text class="method-name">微信支付</text>
            <text class="method-desc">立即支付，调起微信支付流程</text>
          </view>
        </view>
        <view :class="['method-check', selectedMode === 'wxpay' ? 'active' : '']" />
      </view>

      <view
        :class="['pay-method', selectedMode === 'cookNow' ? 'active' : '']"
        @tap="selectedMode = 'cookNow'"
      >
        <view class="method-main">
          <view class="pay-icon warm">炒</view>
          <view>
            <text class="method-name">现炒</text>
            <text class="method-desc">先下单，后端接单后安排现炒制作</text>
          </view>
        </view>
        <view :class="['method-check', selectedMode === 'cookNow' ? 'active' : '']" />
      </view>
    </view>

    <view class="submit-bar">
      <view>
        <text class="final-label">合计</text>
        <text class="final-price">¥{{ totalPrice.toFixed(2) }}</text>
      </view>
      <button class="pay-button" :loading="paying" @tap="submitOrder">
        {{ actionText }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useCart } from '@/composables/useCart'
import { payOrder } from '@/services/payment'
import { submitCookNowOrder } from '@/services/order'

const paying = ref(false)
const selectedMode = ref('wxpay')
const { state, clearCart, totalCount, totalPrice } = useCart()

const items = computed(() => state.items)
const actionText = computed(() => (selectedMode.value === 'cookNow' ? '下单' : '立即支付'))

async function submitOrder() {
  if (!items.value.length) {
    uni.showToast({
      title: '购物车为空',
      icon: 'none'
    })
    return
  }

  paying.value = true

  try {
    const payload = {
      amount: totalPrice.value,
      items: items.value.map((item) => ({
        id: item.id,
        name: item.name,
        count: item.count,
        price: item.price
      }))
    }

    if (selectedMode.value === 'cookNow') {
      const result = await submitCookNowOrder(payload)
      uni.showModal({
        title: '下单结果',
        content: `${result.message}\n订单号：${result.orderNo}`,
        showCancel: false,
        success: () => {
          clearCart()
          uni.reLaunch({
            url: '/pages/menu/index'
          })
        }
      })
      return
    }

    const result = await payOrder(payload, { mock: true })

    uni.showModal({
      title: '支付结果',
      content: `${result.message}\n订单号：${result.orderNo}`,
      showCancel: false,
      success: () => {
        clearCart()
        uni.reLaunch({
          url: '/pages/menu/index'
        })
      }
    })
  } catch (error) {
    uni.showToast({
      title: selectedMode.value === 'cookNow' ? '下单未完成，请稍后重试' : '支付未完成，请稍后重试',
      icon: 'none'
    })
  } finally {
    paying.value = false
  }
}
</script>

<style lang="scss" scoped>
.confirm-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #fff3cb 0%, #f7f4ee 260rpx);
  padding-bottom: 180rpx;
}

.hero {
  padding: 24rpx 24rpx 0;
}

.section {
  margin: 24rpx;
  padding: 26rpx 24rpx;
  background: #fffdf8;
  border-radius: 28rpx;
}

.hero-title {
  display: block;
  color: #2f2418;
  font-size: 42rpx;
  font-weight: 800;
  line-height: 1.3;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.section-title {
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 700;
}

.section-tip {
  color: #a59481;
  font-size: 22rpx;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 18rpx 0;
  border-bottom: 2rpx solid #f4ede2;
}

.order-item:last-child {
  border-bottom: 0;
}

.cover {
  width: 110rpx;
  height: 110rpx;
  border-radius: 18rpx;
  background: #f0eadf;
}

.info {
  flex: 1;
}

.name,
.meta,
.price,
.method-name,
.method-desc {
  display: block;
}

.name {
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 600;
}

.meta,
.method-desc {
  margin-top: 8rpx;
  color: #9f907f;
  font-size: 22rpx;
}

.price {
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 700;
}

.pay-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10rpx 0;
  color: #6b5d4d;
  font-size: 26rpx;
}

.pay-row.total {
  margin-top: 10rpx;
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.pay-method {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18rpx;
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f4ede2;
}

.pay-method:last-of-type {
  border-bottom: 0;
}

.pay-method.active {
  background: rgba(255, 248, 220, 0.35);
}

.method-main {
  display: flex;
  align-items: center;
  gap: 18rpx;
}

.pay-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 20rpx;
  background: #12c15d;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  font-weight: 700;
}

.pay-icon.warm {
  background: #ff9f3f;
}

.method-name {
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 600;
}

.method-check {
  width: 34rpx;
  height: 34rpx;
  border-radius: 17rpx;
  border: 2rpx solid #d6ccbf;
  background: #fff;
  flex-shrink: 0;
}

.method-check.active {
  border-color: #ffb400;
  background: radial-gradient(circle at center, #ffb400 0 42%, #fff 45% 100%);
}

.submit-bar {
  position: fixed;
  left: 20rpx;
  right: 20rpx;
  bottom: 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18rpx 20rpx;
  border-radius: 28rpx;
  background: rgba(255, 253, 248, 0.96);
  backdrop-filter: blur(12px);
}

.final-label {
  color: #8b7c6c;
  font-size: 22rpx;
}

.final-price {
  margin-left: 10rpx;
  color: #ff5d21;
  font-size: 40rpx;
  font-weight: 800;
}

.pay-button {
  margin: 0;
  width: 240rpx;
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
</style>
