<template>
  <view class="order-page">
    <view class="filter-bar">
      <view
        v-for="tab in orderTabs"
        :key="tab.key"
        :class="['filter-chip', activeStatus === tab.key ? 'active' : '']"
        @tap="switchTab(tab.key)"
      >
        {{ tab.label }}
      </view>
    </view>

    <scroll-view class="order-scroll" scroll-y :show-scrollbar="false">
      <view class="order-list">
        <view v-for="order in displayOrders" :key="order.id" class="order-card" @tap="openDetail(order.id)">
          <view class="order-head">
            <view>
              <text class="order-id">订单号 {{ order.id }}</text>
              <text class="order-time">{{ order.time }}</text>
            </view>
            <text class="order-status">{{ order.statusText }}</text>
          </view>
          <view class="order-body">
            <view>
              <text class="order-name">{{ order.title }}</text>
              <text class="order-note">{{ order.note }}</text>
            </view>
            <text class="order-amount">¥{{ order.amount }}</text>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { mockOrders, orderTabs } from '@/mock/orders'

const activeStatus = ref('')

onLoad((options) => {
  activeStatus.value = options.status || ''
})

const displayOrders = computed(() => {
  if (!activeStatus.value) {
    return mockOrders
  }

  return mockOrders.filter((order) => order.status === activeStatus.value)
})

function switchTab(tabKey) {
  activeStatus.value = tabKey
}

function openDetail(orderId) {
  uni.navigateTo({
    url: `/pages/order/detail?id=${orderId}`
  })
}
</script>

<style lang="scss" scoped>
.order-page {
  height: 100vh;
  background: linear-gradient(180deg, #fff6d8 0%, #fffdf8 220rpx);
  overflow: hidden;
}

.filter-bar {
  display: flex;
  gap: 14rpx;
  padding: 22rpx 24rpx 12rpx;
  overflow-x: auto;
  white-space: nowrap;
}

.filter-chip {
  flex-shrink: 0;
  padding: 16rpx 24rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.92);
  color: #8f867e;
  font-size: 24rpx;
}

.filter-chip.active {
  background: linear-gradient(135deg, #ffdf57 0%, #ffc81c 100%);
  color: #5a3c00;
  font-weight: 700;
}

.order-scroll {
  height: calc(100vh - 106rpx);
  padding: 0 24rpx 30rpx;
  box-sizing: border-box;
  scrollbar-width: none;
}

.filter-bar::-webkit-scrollbar,
.order-scroll::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

.filter-bar {
  scrollbar-width: none;
}

.order-card {
  background: #fffdf8;
  border-radius: 28rpx;
}

.order-list {
  padding-top: 20rpx;
}

.order-card {
  margin-bottom: 24rpx;
  padding: 24rpx;
  border: 2rpx solid #f3eadb;
}

.order-head,
.order-body {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.order-body {
  margin-top: 22rpx;
}

.order-id,
.order-time,
.order-status,
.order-name,
.order-note,
.order-amount {
  display: block;
}

.order-id,
.order-name {
  color: #2f2418;
}

.order-id {
  font-size: 24rpx;
  font-weight: 600;
}

.order-time,
.order-note {
  margin-top: 8rpx;
  color: #9f907f;
  font-size: 22rpx;
}

.order-status {
  color: #c98511;
  font-size: 24rpx;
  font-weight: 700;
}

.order-name {
  font-size: 30rpx;
  font-weight: 700;
}

.order-amount {
  color: #ff5d21;
  font-size: 34rpx;
  font-weight: 800;
}

</style>
