<template>
  <view class="page">
    <view class="tab-row">
      <view
        v-for="item in tabs"
        :key="item.key"
        class="tab-item"
        :class="{ active: activeTab === item.key }"
        @tap="activeTab = item.key"
      >
        {{ item.label }}
      </view>
    </view>

    <view v-if="filteredCoupons.length" class="coupon-list">
      <view v-for="item in filteredCoupons" :key="item.id" class="coupon-card">
        <view class="coupon-main">
          <text class="coupon-name">{{ item.name }}</text>
          <text class="coupon-desc">{{ resolveCouponDesc(item) }}</text>
          <text class="coupon-expire">有效期至 {{ item.expireAt }}</text>
        </view>
        <view class="coupon-side">
          <text class="coupon-value">{{ formatValue(item.discountValue) }}</text>
          <text class="coupon-status">{{ item.statusText }}</text>
        </view>
      </view>
    </view>

    <view v-else class="empty-card">
      <text class="empty-title">暂无优惠券</text>
      <text class="empty-desc">下单、活动或会员权益发放后会在这里展示。</text>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { fetchMiniappCoupons } from '@/services/profile'

const tabs = [
  { key: 'available', label: '可使用' },
  { key: 'used', label: '已使用' },
  { key: 'expired', label: '已过期' }
]

const activeTab = ref('available')
const coupons = ref([])

const filteredCoupons = computed(() => coupons.value.filter((item) => {
  if (activeTab.value === 'available') {
    return item.statusText === '可使用'
  }
  if (activeTab.value === 'used') {
    return item.statusText === '已使用'
  }
  return item.statusText === '已过期'
}))

function formatValue(value) {
  if (value === null || value === undefined) {
    return '¥0'
  }
  return `¥${Number(value).toFixed(0)}`
}

function resolveCouponDesc(item) {
  if (item.type === 'cash') {
    return `满 ${formatValue(item.threshold)} 可用，立减 ${formatValue(item.discountValue)}`
  }
  return `单品抵扣 ${formatValue(item.discountValue)}`
}

async function loadData() {
  try {
    coupons.value = await fetchMiniappCoupons()
  } catch (error) {
    uni.showToast({
      title: error?.message || '优惠券加载失败',
      icon: 'none'
    })
  }
}

onShow(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  padding: 24rpx;
  background: #fdfaf4;
  box-sizing: border-box;
}

.tab-row {
  display: flex;
  gap: 16rpx;
}

.tab-item {
  min-width: 160rpx;
  padding: 18rpx 0;
  border-radius: 999rpx;
  background: #fff2d8;
  color: #8f6a24;
  font-size: 26rpx;
  text-align: center;
}

.tab-item.active {
  background: linear-gradient(135deg, #ffde56 0%, #ffc818 100%);
  color: #5a3c00;
  font-weight: 700;
}

.coupon-list {
  margin-top: 22rpx;
  display: flex;
  flex-direction: column;
  gap: 18rpx;
}

.coupon-card,
.empty-card {
  display: flex;
  justify-content: space-between;
  gap: 20rpx;
  padding: 26rpx 24rpx;
  border-radius: 28rpx;
  background: #ffffff;
}

.coupon-main {
  flex: 1;
  min-width: 0;
}

.coupon-name {
  display: block;
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.coupon-desc,
.coupon-expire {
  display: block;
  margin-top: 12rpx;
  color: #8e7f6e;
  font-size: 24rpx;
}

.coupon-side {
  align-self: center;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 10rpx;
}

.coupon-value {
  color: #ff7f1f;
  font-size: 40rpx;
  font-weight: 800;
}

.coupon-status {
  color: #c98511;
  font-size: 24rpx;
}

.empty-card {
  margin-top: 22rpx;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 24rpx;
}

.empty-title {
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.empty-desc {
  margin-top: 14rpx;
  color: #9f907f;
  font-size: 24rpx;
  text-align: center;
}
</style>
