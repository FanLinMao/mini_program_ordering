<template>
  <view class="page">
    <view class="hero-card">
      <text class="hero-title">我的权益</text>
      <text class="hero-desc">积分、会员和节省金额都会随着下单与用券实时累计。</text>
    </view>

    <view class="grid">
      <view v-for="item in benefitItems" :key="item.key" class="benefit-card">
        <text class="benefit-value">{{ item.value }}</text>
        <text class="benefit-label">{{ item.label }}</text>
        <text class="benefit-desc">{{ item.desc }}</text>
      </view>
    </view>

    <view class="section-card">
      <text class="section-title">权益说明</text>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">会员积分可在活动期间兑换菜品券或抵扣券。</text>
      </view>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">本月节省按照已使用优惠券累计展示，方便回看省下的金额。</text>
      </view>
      <view class="tip-row">
        <text class="tip-dot"></text>
        <text class="tip-text">会员等级会根据后续活动与消费情况逐步升级。</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { fetchProfileHome } from '@/services/profile'

const homeData = ref({
  points: 0,
  couponCount: 0,
  savingsAmount: '¥0',
  memberLevel: '普通会员'
})

const benefitItems = computed(() => [
  {
    key: 'points',
    value: `${homeData.value.points ?? 0}`,
    label: '会员积分',
    desc: '可兑换菜品券'
  },
  {
    key: 'coupons',
    value: `${homeData.value.couponCount ?? 0}`,
    label: '可用优惠券',
    desc: '满减券与单品券'
  },
  {
    key: 'savings',
    value: homeData.value.savingsAmount || '¥0',
    label: '本月节省',
    desc: '已累计优惠金额'
  },
  {
    key: 'level',
    value: homeData.value.memberLevel || '普通会员',
    label: '会员等级',
    desc: '尊享优先出餐'
  }
])

async function loadData() {
  try {
    homeData.value = {
      ...homeData.value,
      ...(await fetchProfileHome())
    }
  } catch (error) {
    uni.showToast({
      title: error?.message || '权益加载失败',
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

.hero-title {
  display: block;
  color: #2f2418;
  font-size: 34rpx;
  font-weight: 700;
}

.hero-desc {
  display: block;
  margin-top: 12rpx;
  color: #8e7f6e;
  font-size: 24rpx;
  line-height: 1.6;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20rpx;
  margin-top: 20rpx;
}

.benefit-card {
  padding: 24rpx 22rpx;
  border-radius: 24rpx;
  background: linear-gradient(180deg, #fff8e2 0%, #fffdf8 100%);
}

.benefit-value {
  display: block;
  color: #ff8a22;
  font-size: 40rpx;
  font-weight: 800;
}

.benefit-label {
  display: block;
  margin-top: 12rpx;
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 700;
}

.benefit-desc {
  display: block;
  margin-top: 8rpx;
  color: #9f907f;
  font-size: 22rpx;
}

.section-card {
  margin-top: 20rpx;
}

.section-title {
  display: block;
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.tip-row {
  display: flex;
  gap: 14rpx;
  margin-top: 20rpx;
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
