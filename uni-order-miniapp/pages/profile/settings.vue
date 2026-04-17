<template>
  <view class="page">
    <view class="section-card">
      <text class="section-title">账号设置</text>
      <view class="setting-row">
        <text class="setting-label">当前用户</text>
        <text class="setting-value">{{ displayName }}</text>
      </view>
      <view class="setting-row">
        <text class="setting-label">登录状态</text>
        <text class="setting-value">{{ isLoggedIn ? '已登录' : '未登录' }}</text>
      </view>
    </view>

    <view class="section-card">
      <text class="section-title">支付方式</text>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">当前支持微信支付与现炒下单两种方式。</text>
      </view>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">现炒下单会先进入待支付，后续支付完成即可进入制作流程。</text>
      </view>
    </view>

    <view class="section-card">
      <text class="section-title">通知与隐私</text>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">登录后会同步昵称、头像和订单信息，用于展示与售后服务。</text>
      </view>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">后续版本会继续补充消息通知与隐私条款详情页。</text>
      </view>
    </view>

    <button v-if="isLoggedIn" class="logout-button" @tap="handleLogout">退出登录</button>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getMiniappUser, logoutMiniappUser } from '@/services/auth'

const user = ref(getMiniappUser())

const isLoggedIn = computed(() => !!(user.value?.userId && user.value?.openid))
const displayName = computed(() => user.value?.nickname || '微信用户')

async function handleLogout() {
  try {
    await logoutMiniappUser()
    uni.showToast({
      title: '已退出登录',
      icon: 'none'
    })
    uni.reLaunch({
      url: '/pages/menu/index?tab=profile'
    })
  } catch (error) {
    uni.showToast({
      title: error?.message || '退出登录失败',
      icon: 'none'
    })
  }
}

onShow(() => {
  user.value = getMiniappUser()
})
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  padding: 24rpx;
  background: #fdfaf4;
  box-sizing: border-box;
}

.section-card {
  margin-bottom: 20rpx;
  padding: 28rpx 24rpx;
  background: #ffffff;
  border-radius: 28rpx;
}

.section-title {
  display: block;
  color: #2f2418;
  font-size: 32rpx;
  font-weight: 700;
}

.setting-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20rpx;
  padding-top: 20rpx;
}

.setting-label {
  color: #6b5d4d;
  font-size: 24rpx;
}

.setting-value {
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

.logout-button {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 24rpx;
  background: #ffffff;
  color: #b46a32;
  font-size: 30rpx;
  font-weight: 700;
}

.logout-button::after {
  border: 0;
}
</style>
