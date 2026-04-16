<template>
  <view class="tab-bar safe-bottom">
    <view
      v-for="item in items"
      :key="item.key"
      :class="['tab-item', activeKey === item.key ? 'active' : '']"
      @tap="$emit('change', item.key)"
    >
      <view class="icon-wrap">
        <uni-icons :type="item.icon" size="20" :color="activeKey === item.key ? '#5a3c00' : '#8f867e'" />
        <uni-badge v-if="item.badge" :text="item.badge" absolute />
      </view>
      <text class="tab-label">{{ item.label }}</text>
    </view>
  </view>
</template>

<script setup>
defineProps({
  activeKey: {
    type: String,
    default: 'menu'
  },
  items: {
    type: Array,
    default: () => []
  }
})

defineEmits(['change'])
</script>

<style lang="scss" scoped>
.tab-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: calc(108rpx + env(safe-area-inset-bottom));
  background: rgba(255, 253, 248, 0.96);
  backdrop-filter: blur(14px);
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 8rpx 20rpx calc(env(safe-area-inset-bottom) + 6rpx);
  box-sizing: border-box;
  z-index: 30;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  padding-top: 6rpx;
  color: #8f867e;
}

.tab-item.active {
  color: #5a3c00;
}

.icon-wrap {
  position: relative;
  width: 44rpx;
  height: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tab-label {
  font-size: 22rpx;
  line-height: 1;
}
</style>
