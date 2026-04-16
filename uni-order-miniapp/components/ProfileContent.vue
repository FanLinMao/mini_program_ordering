<template>
  <view class="sub-page" :style="subPageStyle">
    <scroll-view class="sub-scroll" scroll-y :show-scrollbar="false">
      <view class="account-hero" @tap="handleHeroTap">
        <view class="hero-main">
          <view v-if="isLoggedIn && userInfo.avatar" class="profile-avatar image-avatar">
            <image class="avatar-image" :src="userInfo.avatar" mode="aspectFill" />
          </view>
          <view v-else class="profile-avatar empty-avatar">
            <view class="empty-avatar-icon">
              <view class="empty-avatar-head"></view>
              <view class="empty-avatar-body"></view>
            </view>
          </view>
          <view class="profile-main">
            <text class="profile-name">{{ displayName }}</text>
            <text class="profile-id">{{ profileDesc }}</text>
          </view>
        </view>
        <view class="hero-stats">
          <view class="hero-stat">
            <text class="hero-stat-value">{{ couponCount }}</text>
            <text class="hero-stat-label">优惠券</text>
          </view>
          <view class="hero-stat">
            <text class="hero-stat-value">{{ savingText }}</text>
            <text class="hero-stat-label">本月节省</text>
          </view>
        </view>
      </view>

      <view class="section-card">
        <view class="info-row">
          <text class="info-label">我的订单</text>
          <text class="info-value action-text" @tap="$emit('open-order-list', '')">全部订单</text>
        </view>
        <view class="order-shortcuts">
          <view
            v-for="item in orderShortcuts"
            :key="item.label"
            class="shortcut-item"
            @tap="$emit('open-order-list', item.key)"
          >
            <view class="shortcut-icon">{{ item.icon }}</view>
            <text class="shortcut-label">{{ item.label }}</text>
            <text class="shortcut-count">{{ item.count }}</text>
          </view>
        </view>
      </view>

      <view class="section-card">
        <view class="info-row">
          <text class="info-label">我的权益</text>
        </view>
        <view class="benefit-grid">
          <view v-for="item in benefitItems" :key="item.label" class="benefit-item">
            <text class="benefit-value">{{ item.value }}</text>
            <text class="benefit-label">{{ item.label }}</text>
            <text class="benefit-desc">{{ item.desc }}</text>
          </view>
        </view>
      </view>

      <view class="section-card">
        <view class="info-row">
          <text class="info-label">常用服务</text>
        </view>
        <view v-for="item in serviceItems" :key="item.label" class="service-row">
          <view>
            <text class="service-label">{{ item.label }}</text>
            <text class="service-desc">{{ item.desc }}</text>
          </view>
          <text class="service-arrow">›</text>
        </view>
      </view>

      <view class="logout-wrap">
        <button
          v-if="isLoggedIn"
          class="logout-button"
          @tap="$emit('logout')"
        >
          退出登录
        </button>
        <button
          v-else
          class="logout-button login-button"
          @tap="$emit('login')"
        >
          微信登录 / 注册
        </button>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  subPageStyle: {
    type: Object,
    default: () => ({})
  },
  userInfo: {
    type: Object,
    default: () => ({})
  },
  isLoggedIn: {
    type: Boolean,
    default: false
  },
  orderShortcuts: {
    type: Array,
    default: () => []
  },
  benefitItems: {
    type: Array,
    default: () => []
  },
  serviceItems: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['open-order-list', 'logout', 'login'])

const displayName = computed(() => {
  if (!props.isLoggedIn) return '登录/注册'
  return props.userInfo.nickname || '微信用户'
})

const profileDesc = computed(() => {
  if (!props.isLoggedIn) return '登录后可同步订单、权益与会员信息'
  const level = props.userInfo.memberLevel || '普通会员'
  const points = props.userInfo.points ?? 0
  return `${level} · 积分 ${points}`
})

const couponCount = computed(() => {
  if (!props.isLoggedIn) return '--'
  return '6'
})

const savingText = computed(() => {
  if (!props.isLoggedIn) return '--'
  return '¥58'
})

function handleHeroTap() {
  if (!props.isLoggedIn) {
    emit('login')
  }
}
</script>

<style lang="scss" scoped>
.sub-page {
  height: calc(100vh - env(safe-area-inset-bottom) - 108rpx);
  background: #fdfaf4;
  padding: 34rpx 24rpx 0;
  box-sizing: border-box;
}

.sub-scroll {
  height: 100%;
  background: #fdfaf4;
  padding-bottom: 24rpx;
  box-sizing: border-box;
  scrollbar-width: none;
}

.sub-scroll::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

:deep(.uni-scroll-view::-webkit-scrollbar),
:deep(.uni-scroll-view-scrollbar),
:deep(.uni-scroll-view-scrollbar-y),
:deep(.uni-scroll-view-scrollbar-thumb) {
  display: none !important;
  width: 0 !important;
  height: 0 !important;
  opacity: 0 !important;
}

:deep(.uni-scroll-view) {
  scrollbar-width: none !important;
  -ms-overflow-style: none !important;
}

.account-hero {
  overflow: hidden;
  border-radius: 32rpx;
  background: linear-gradient(135deg, #2f2418 0%, #5a3c00 100%);
  color: #fff8ec;
}

.hero-main {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 28rpx 24rpx 24rpx;
}

.profile-avatar {
  width: 96rpx;
  height: 96rpx;
  border-radius: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  overflow: hidden;
}

.image-avatar {
  background: #fffdf8;
}

.avatar-image {
  width: 100%;
  height: 100%;
}

.empty-avatar {
  background: rgba(255, 255, 255, 0.14);
  border: 2rpx solid rgba(255, 255, 255, 0.26);
}

.empty-avatar-icon {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.empty-avatar-head {
  width: 26rpx;
  height: 26rpx;
  border-radius: 13rpx;
  background: #fff8ec;
}

.empty-avatar-body {
  width: 46rpx;
  height: 26rpx;
  border-radius: 24rpx 24rpx 18rpx 18rpx;
  background: #fff8ec;
}

.profile-main {
  min-width: 0;
}

.profile-name {
  display: block;
  font-size: 32rpx;
  font-weight: 700;
  color: #fffdf8;
}

.profile-id {
  display: block;
  margin-top: 8rpx;
  color: rgba(255, 248, 236, 0.76);
  font-size: 22rpx;
}

.hero-stats {
  display: flex;
  border-top: 2rpx solid rgba(255, 255, 255, 0.08);
}

.hero-stat {
  flex: 1;
  padding: 22rpx 0 26rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.hero-stat-value {
  color: #ffdf57;
  font-size: 32rpx;
  font-weight: 800;
}

.hero-stat-label {
  color: rgba(255, 248, 236, 0.72);
  font-size: 22rpx;
}

.section-card {
  margin-top: 22rpx;
  padding: 12rpx 22rpx;
  background: #ffffff;
  border-radius: 28rpx;
  overflow: hidden;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 18rpx 0;
}

.info-label,
.info-value,
.benefit-value,
.benefit-label,
.benefit-desc,
.service-label,
.service-desc,
.service-arrow {
  display: block;
}

.info-label {
  color: #2f2418;
}

.info-value {
  color: #6b5d4d;
  font-size: 24rpx;
}

.action-text {
  color: #c98511;
  font-weight: 700;
}

.order-shortcuts {
  display: flex;
  justify-content: space-between;
  gap: 12rpx;
  padding: 10rpx 0 6rpx;
}

.shortcut-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
}

.shortcut-icon {
  width: 68rpx;
  height: 68rpx;
  border-radius: 34rpx;
  background: #fff3cb;
  color: #8a5b00;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  font-weight: 700;
}

.shortcut-label {
  color: #6b5d4d;
  font-size: 22rpx;
}

.shortcut-count {
  color: #ff5d21;
  font-size: 22rpx;
  font-weight: 700;
}

.benefit-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18rpx;
  padding: 12rpx 0 18rpx;
}

.benefit-item {
  padding: 22rpx 20rpx;
  border-radius: 24rpx;
  background: linear-gradient(180deg, #fff8e2 0%, #fffdf8 100%);
}

.benefit-value {
  color: #ff8a22;
  font-size: 36rpx;
  font-weight: 800;
}

.benefit-label {
  margin-top: 8rpx;
  color: #2f2418;
  font-size: 26rpx;
  font-weight: 700;
}

.benefit-desc {
  margin-top: 8rpx;
  color: #9f907f;
  font-size: 22rpx;
  line-height: 1.4;
}

.service-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 20rpx 0;
  border-top: 2rpx solid #f2ebdf;
}

.service-label {
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 600;
}

.service-desc {
  margin-top: 8rpx;
  color: #9f907f;
  font-size: 22rpx;
}

.service-arrow {
  color: #b0a18f;
  font-size: 34rpx;
}

.logout-wrap {
  padding-top: 24rpx;
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

.login-button {
  background: linear-gradient(135deg, #ffde56 0%, #ffc818 100%);
  color: #5a3c00;
}
</style>
