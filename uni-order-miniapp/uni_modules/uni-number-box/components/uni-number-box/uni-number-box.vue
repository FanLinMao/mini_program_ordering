<template>
  <view class="number-box">
    <view class="action" @tap="changeValue(-1)">-</view>
    <text class="value">{{ currentValue }}</text>
    <view class="action active" @tap="changeValue(1)">+</view>
  </view>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: Number,
    default: 0
  },
  min: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

const currentValue = computed(() => props.modelValue)

function changeValue(step) {
  const nextValue = Math.max(props.min, props.modelValue + step)
  emit('update:modelValue', nextValue)
  emit('change', nextValue)
}
</script>

<style lang="scss" scoped>
.number-box {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.action {
  width: 44rpx;
  height: 44rpx;
  border-radius: 22rpx;
  border: 2rpx solid #eadfd2;
  color: #7f6d57;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
}

.action.active {
  background: #ffcf22;
  color: #2f2418;
  border-color: transparent;
}

.value {
  min-width: 26rpx;
  text-align: center;
  color: #2f2418;
  font-size: 24rpx;
}
</style>
