<template>
  <view class="page">
    <view class="hero-card">
      <text class="hero-title">{{ serviceTitle }}</text>
      <text class="hero-desc">{{ serviceNotice }}</text>
    </view>

    <view class="section-card">
      <view class="info-row">
        <text class="info-label">服务时间</text>
        <text class="info-value">{{ serviceHours }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">服务说明</text>
        <text class="info-value">{{ serviceSummary }}</text>
      </view>
    </view>

    <view class="section-card">
      <text class="section-title">当前版本说明</text>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">订单、退款售后与地址问题都可以先通过店铺客服处理。</text>
      </view>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">本版本先上线基础客服信息展示，后面可以继续接在线会话或电话客服。</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getMiniappUser } from '@/services/auth'
import { fetchProfileHome } from '@/services/profile'

const homeData = ref({
  serviceTitle: '私人厨房',
  serviceHours: '在线服务中，平均 30 秒响应',
  serviceNotice: '点完即做菜，欢迎随时咨询订单与售后问题',
  serviceSummary: '在线服务中，平均 30 秒响应'
})

const serviceTitle = computed(() => homeData.value.serviceTitle || '私人厨房')
const serviceHours = computed(() => homeData.value.serviceHours || '在线服务中，平均 30 秒响应')
const serviceNotice = computed(() => homeData.value.serviceNotice || '点完即做菜，欢迎随时咨询订单与售后问题')
const serviceSummary = computed(() => homeData.value.serviceSummary || serviceHours.value)

async function loadData() {
  const user = getMiniappUser()
  if (!user?.userId || !user?.openid) {
    return
  }
  try {
    homeData.value = {
      ...homeData.value,
      ...(await fetchProfileHome())
    }
  } catch (error) {
    uni.showToast({
      title: error?.message || '客服信息加载失败',
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

.hero-card,
.section-card {
  padding: 28rpx 24rpx;
  background: #ffffff;
  border-radius: 28rpx;
}

.section-card {
  margin-top: 20rpx;
}

.hero-title,
.section-title {
  display: block;
  color: #2f2418;
  font-size: 32rpx;
  font-weight: 700;
}

.hero-desc {
  display: block;
  margin-top: 14rpx;
  color: #8e7f6e;
  font-size: 24rpx;
  line-height: 1.7;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20rpx;
  padding: 18rpx 0;
  border-top: 2rpx solid #f2ebdf;
}

.info-row:first-of-type {
  margin-top: 8rpx;
}

.info-label {
  color: #6b5d4d;
  font-size: 24rpx;
}

.info-value {
  flex: 1;
  text-align: right;
  color: #2f2418;
  font-size: 24rpx;
}

.tip-row {
  display: flex;
  gap: 14rpx;
  margin-top: 18rpx;
  align-items: flex-start;
}

.tip-dot {
  width: 12rpx;
  height: 12rpx;
  margin-top: 12rpx;
  border-radius: 50%;
  background: #ffbd32;
  flex-shrink: 0;
}

.tip-text {
  flex: 1;
  color: #6b5d4d;
  font-size: 24rpx;
  line-height: 1.7;
}
</style>
