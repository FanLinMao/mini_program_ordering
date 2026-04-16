<template>
  <view class="checkout-bar safe-bottom" :style="barStyle">
    <view class="cart-entry" @tap="$emit('open-cart')">
      <view class="cart-icon">
        <uni-icons type="cart" size="26" color="#7f6d57" />
        <uni-badge v-if="totalCount > 0" :text="`${totalCount}`" absolute />
      </view>
      <view class="cart-copy">
        <text class="delivery">{{ deliveryText }}</text>
        <text class="summary" v-if="totalCount > 0">已选 {{ totalCount }} 件，合计 ¥{{ totalPrice.toFixed(2) }}</text>
        <text class="summary" v-else>先挑几道招牌菜吧</text>
      </view>
    </view>

    <view
      :class="['submit-button', canCheckout ? 'active' : '']"
      @tap="$emit('checkout')"
    >
      <text>下单</text>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  totalCount: {
    type: Number,
    default: 0
  },
  totalPrice: {
    type: Number,
    default: 0
  },
  amountToStart: {
    type: Number,
    default: 0
  },
  canCheckout: {
    type: Boolean,
    default: false
  },
  deliveryText: {
    type: String,
    default: '免配送费'
  },
  bottomOffset: {
    type: Number,
    default: 18
  }
})

defineEmits(['checkout', 'open-cart'])

const barStyle = computed(() => ({
  bottom: `${props.bottomOffset}rpx`
}))
</script>

<style lang="scss" scoped>
.checkout-bar {
  position: fixed;
  left: 20rpx;
  right: 20rpx;
  height: 112rpx;
  background: rgba(250, 245, 236, 0.94);
  backdrop-filter: blur(10px);
  border-radius: 56rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 18rpx 0 10rpx;
}

.cart-entry {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.cart-icon {
  position: relative;
  width: 84rpx;
  height: 84rpx;
  border-radius: 42rpx;
  background: linear-gradient(180deg, #efe7da 0%, #d7cab7 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.cart-copy {
  display: flex;
  flex-direction: column;
  gap: 6rpx;
}

.delivery {
  color: #726251;
  font-size: 24rpx;
}

.summary {
  color: #a39483;
  font-size: 22rpx;
}

.submit-button {
  min-width: 200rpx;
  height: 84rpx;
  border-radius: 42rpx;
  padding: 0 24rpx;
  background: #d9d5cf;
  color: #8f867e;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  font-weight: 700;
}

.submit-button.active {
  background: linear-gradient(135deg, #ffdf57 0%, #ffc81c 100%);
  color: #5a3c00;
}
</style>
