<template>
  <view class="menu-content">
    <view class="main-layout">
      <scroll-view class="sidebar" scroll-y enhanced :show-scrollbar="false">
        <view
          v-for="item in categories"
          :key="item.id"
          :class="['category-item', activeCategoryId === item.id ? 'active' : '']"
          @tap="$emit('switch-category', item.id)"
        >
          <view v-if="item.tag" class="category-tag">{{ item.tag }}</view>
          <text class="category-name">{{ item.name }}</text>
        </view>
      </scroll-view>

      <scroll-view class="dish-panel" scroll-y enhanced :show-scrollbar="false">
        <view v-for="category in visibleCategories" :key="category.id" class="category-block">
          <text class="section-title">{{ category.name }}</text>
          <view v-for="dish in category.dishes" :key="dish.id" class="dish-card">
            <image class="cover" :src="dish.image" mode="aspectFill" />
            <view class="content">
              <text class="name">{{ dish.name }}</text>
              <text class="meta">月售 {{ dish.monthlySales }}+ · {{ dish.praise }}</text>
              <text class="sub-meta">{{ dish.extra }}</text>
              <view class="bottom-row">
                <text class="price">¥{{ dish.price }}</text>
                <view class="stepper">
                  <view
                    v-if="getDishCount(dish.id) > 0"
                    class="minus"
                    @tap.stop="$emit('decrease-dish', dish.id)"
                  >
                    <uni-icons type="minus" size="14" color="#8b7c6c" />
                  </view>
                  <text v-if="getDishCount(dish.id) > 0" class="count">{{ getDishCount(dish.id) }}</text>
                  <view class="plus" @tap.stop="$emit('add-dish', dish)">
                    <uni-icons type="plus" size="16" color="#2f2418" />
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <CheckoutBar
      :total-count="totalCount"
      :total-price="totalPrice"
      :amount-to-start="amountToStart"
      :can-checkout="canCheckout"
      :delivery-text="deliveryText"
      :bottom-offset="194"
      @open-cart="$emit('open-cart')"
      @checkout="$emit('checkout')"
    />
  </view>
</template>

<script setup>
import CheckoutBar from '@/components/CheckoutBar.vue'

const props = defineProps({
  categories: {
    type: Array,
    default: () => []
  },
  visibleCategories: {
    type: Array,
    default: () => []
  },
  activeCategoryId: {
    type: String,
    default: ''
  },
  cartItems: {
    type: Array,
    default: () => []
  },
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
  }
})

defineEmits(['switch-category', 'add-dish', 'decrease-dish', 'open-cart', 'checkout'])

function getDishCount(dishId) {
  const item = props.cartItems.find((cartItem) => cartItem.id === dishId)
  return item ? item.count : 0
}
</script>

<style lang="scss" scoped>
.menu-content {
  height: calc(100vh - 128rpx);
}

.main-layout {
  display: flex;
  height: 100%;
}

.sidebar {
  width: 156rpx;
  height: 100%;
  background: #f3efe8;
  scrollbar-width: none;
}

.dish-panel {
  flex: 1;
  height: 100%;
  padding: 0 24rpx 180rpx;
  scrollbar-width: none;
}

.sidebar::-webkit-scrollbar,
.dish-panel::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

.category-item {
  min-height: 108rpx;
  padding: 18rpx 18rpx 18rpx 20rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 8rpx;
  color: #6b5d4d;
}

.category-item.active {
  background: #fffdf8;
  color: #2f2418;
  font-weight: 700;
  border-left: 6rpx solid #ffcc00;
}

.category-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: fit-content;
  max-width: 100%;
  padding: 4rpx 10rpx;
  border-radius: 999rpx;
  background: #fff2c4;
  color: #a16d00;
  font-size: 18rpx;
}

.category-name {
  font-size: 24rpx;
  line-height: 1.35;
}

.category-block {
  padding-bottom: 8rpx;
}

.section-title {
  display: block;
  margin: 12rpx 0 8rpx;
  color: #2f2418;
  font-size: 28rpx;
  font-weight: 700;
}

.dish-card {
  display: flex;
  gap: 18rpx;
  padding: 22rpx 0;
}

.cover {
  width: 176rpx;
  height: 132rpx;
  border-radius: 22rpx;
  background: #f0eadf;
  flex-shrink: 0;
}

.content {
  flex: 1;
  min-width: 0;
}

.name {
  display: block;
  font-size: 32rpx;
  line-height: 1.3;
  color: #2f2418;
  font-weight: 700;
}

.meta,
.sub-meta {
  display: block;
  margin-top: 10rpx;
  font-size: 22rpx;
  line-height: 1.3;
}

.meta {
  color: #8f806f;
}

.sub-meta {
  color: #c28a21;
}

.bottom-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16rpx;
}

.price {
  color: #ff5d21;
  font-size: 38rpx;
  font-weight: 800;
}

.stepper {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.plus,
.minus {
  width: 46rpx;
  height: 46rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.plus {
  background: #ffcf22;
  box-shadow: 0 10rpx 24rpx rgba(255, 207, 34, 0.32);
}

.minus {
  border: 2rpx solid #eadfd2;
  background: #fff;
}

.count {
  min-width: 24rpx;
  text-align: center;
  color: #5f5244;
  font-size: 24rpx;
}
</style>
