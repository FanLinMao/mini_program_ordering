<template>
  <uni-popup ref="popupRef" type="bottom">
    <view class="cart-popup">
      <view class="popup-header">
        <text class="title">已选商品</text>
        <text class="clear" @tap="handleClear">清空</text>
      </view>

      <view v-if="items.length === 0" class="empty">购物车还是空的，先去选几道菜吧。</view>

      <scroll-view v-else class="list" scroll-y>
        <view v-for="item in items" :key="item.id" class="item">
          <view>
            <text class="item-name">{{ item.name }}</text>
            <text class="item-price">¥{{ item.price }}</text>
          </view>
          <uni-number-box
            :modelValue="item.count"
            :min="0"
            @change="(value) => $emit('change-count', { id: item.id, value })"
          />
        </view>
      </scroll-view>
    </view>
  </uni-popup>
</template>

<script setup>
import { ref } from 'vue'

const popupRef = ref(null)

defineProps({
  items: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['clear', 'change-count'])

function open() {
  if (popupRef.value) {
    popupRef.value.open()
  }
}

function close() {
  if (popupRef.value) {
    popupRef.value.close()
  }
}

function handleClear() {
  emit('clear')
  close()
}

defineExpose({
  open,
  close
})
</script>

<style lang="scss" scoped>
.cart-popup {
  background: #fffdf8;
  border-radius: 36rpx 36rpx 0 0;
  padding: 28rpx 24rpx calc(env(safe-area-inset-bottom) + 32rpx);
}

.popup-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24rpx;
}

.title {
  font-size: 30rpx;
  font-weight: 700;
  color: #2f2418;
}

.clear {
  font-size: 24rpx;
  color: #9f907f;
}

.empty {
  padding: 32rpx 0 12rpx;
  color: #9f907f;
  text-align: center;
}

.list {
  max-height: 700rpx;
}

.item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 2rpx solid #f2ebdf;
}

.item-name,
.item-price {
  display: block;
}

.item-name {
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 600;
}

.item-price {
  margin-top: 8rpx;
  color: #ff5d21;
  font-size: 26rpx;
}
</style>
