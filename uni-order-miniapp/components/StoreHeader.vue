<template>
  <view class="header" :style="headerStyle">
    <view class="title-row" :style="titleRowStyle">
      <text class="page-title">小灶点餐</text>
    </view>

    <view class="search-row">
      <view class="search-box">
        <uni-icons type="search" size="30" color="#9f907f" />
        <input
          v-model="keyword"
          class="search-input"
          confirm-type="search"
          placeholder="请输入菜名"
          placeholder-class="search-placeholder"
          @confirm="handleSearch"
        />
      </view>
      <view class="search-button" @tap="handleSearch">搜索</view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'

defineProps({
  shopInfo: {
    type: Object,
    required: true
  }
})

const systemInfo = uni.getSystemInfoSync()
const statusBarHeight = systemInfo.statusBarHeight || 24
const menuButtonRect = typeof uni.getMenuButtonBoundingClientRect === 'function'
  ? uni.getMenuButtonBoundingClientRect()
  : null

const navHeight = menuButtonRect?.height || 32
const navTopGap = menuButtonRect?.top ? Math.max(menuButtonRect.top - statusBarHeight, 6) : 8

const headerStyle = computed(() => ({
  paddingTop: `${statusBarHeight + navTopGap}px`
}))

const titleRowStyle = computed(() => ({
  minHeight: `${navHeight}px`
}))

const keyword = ref('')

function handleSearch() {
  uni.showToast({
    title: keyword.value ? `搜索：${keyword.value}` : '请输入菜名',
    icon: 'none'
  })
}
</script>

<style lang="scss" scoped>
.header {
  padding: 0 24rpx 16rpx;
  background: linear-gradient(180deg, #fff7df 0%, #fffdf8 100%);
}

.title-row {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-title {
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.search-row {
  margin-top: 16rpx;
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.search-box {
  flex: 1;
  height: 68rpx;
  border-radius: 34rpx;
  background: rgba(255, 255, 255, 0.92);
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 0 20rpx;
}

.search-input {
  flex: 1;
  height: 68rpx;
  color: #5f5244;
  font-size: 28rpx;
}

.search-placeholder {
  color: #b2a08d;
  font-size: 28rpx;
}

.search-button {
  width: 120rpx;
  height: 68rpx;
  border-radius: 34rpx;
  background: linear-gradient(135deg, #ffdf57 0%, #ffc81c 100%);
  color: #5a3c00;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  font-weight: 700;
  flex-shrink: 0;
}
</style>
