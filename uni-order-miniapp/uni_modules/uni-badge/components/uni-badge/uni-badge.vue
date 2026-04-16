<template>
  <view :class="['badge', absolute ? 'absolute' : '']">
    <text class="badge-text">{{ text }}</text>
  </view>
</template>

<script setup>
defineProps({
  text: {
    type: [String, Number],
    default: ''
  },
  absolute: {
    type: Boolean,
    default: false
  }
})
</script>

<style lang="scss" scoped>
.badge {
  min-width: 34rpx;
  height: 34rpx;
  padding: 0 8rpx;
  border-radius: 17rpx;
  background: #ff5d21;
  display: flex;
  align-items: center;
  justify-content: center;
}

.badge.absolute {
  position: absolute;
  top: -6rpx;
  right: -4rpx;
}

.badge-text {
  color: #fff;
  font-size: 20rpx;
  line-height: 1;
}
</style>
