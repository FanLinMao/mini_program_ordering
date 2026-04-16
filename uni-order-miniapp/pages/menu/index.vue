<template>
  <view class="page">
    <StoreHeader v-if="activeTab === 'menu'" :shop-info="shopInfo" />

    <MenuContent
      v-if="activeTab === 'menu'"
      :categories="dishCategories"
      :visible-categories="visibleCategories"
      :active-category-id="activeCategoryId"
      :cart-items="cartItems"
      :total-count="totalCount"
      :total-price="totalPrice"
      :amount-to-start="amountToStart"
      :can-checkout="canCheckout"
      :delivery-text="shopInfo.deliveryText"
      @switch-category="switchCategory"
      @add-dish="handleAdd"
      @decrease-dish="handleDecrease"
      @open-cart="openCart"
      @checkout="goCheckout"
    />

    <ProfileContent
      v-else
      :sub-page-style="subPageStyle"
      :order-shortcuts="orderShortcuts"
      :benefit-items="benefitItems"
      :service-items="serviceItems"
      @open-order-list="openOrderList"
      @logout="handleLogout"
    />

    <BottomTabBar
      :active-key="activeTab"
      :items="tabItems"
      @change="switchTab"
    />

    <CartPopup
      ref="cartPopupRef"
      :items="cartItems"
      @clear="clearCart"
      @change-count="handlePopupCountChange"
    />
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import BottomTabBar from '@/components/BottomTabBar.vue'
import CartPopup from '@/components/CartPopup.vue'
import MenuContent from '@/components/MenuContent.vue'
import ProfileContent from '@/components/ProfileContent.vue'
import StoreHeader from '@/components/StoreHeader.vue'
import { useCart } from '@/composables/useCart'
import { dishCategories, shopInfo } from '@/mock/menu'
import { mockOrders, orderTabs } from '@/mock/orders'

const systemInfo = uni.getSystemInfoSync()
const statusBarHeight = systemInfo.statusBarHeight || 24
const menuButtonRect = typeof uni.getMenuButtonBoundingClientRect === 'function'
  ? uni.getMenuButtonBoundingClientRect()
  : null
const capsuleHeight = menuButtonRect && menuButtonRect.height ? menuButtonRect.height : 32
const capsuleTop = menuButtonRect && menuButtonRect.top ? menuButtonRect.top : statusBarHeight + 8
const subPageTopPadding = capsuleTop + capsuleHeight + 16

const activeTab = ref('menu')
const activeCategoryId = ref(dishCategories.length ? dishCategories[0].id : '')
const cartPopupRef = ref(null)
const { state, addDish, decreaseDish, clearCart, totalCount, totalPrice, amountToStart, canCheckout } = useCart()

const cartItems = computed(() => state.items)
const visibleCategories = computed(() => {
  const currentCategory = dishCategories.find((category) => category.id === activeCategoryId.value)
  return currentCategory ? [currentCategory] : []
})
const tabItems = computed(() => [
  { key: 'menu', label: '点餐', icon: 'shop' },
  { key: 'profile', label: '我的', icon: 'person' }
])
const subPageStyle = computed(() => ({
  paddingTop: `${subPageTopPadding}px`
}))
const orderShortcutIconMap = {
  pendingPay: '付',
  pickup: '取',
  review: '评',
  refund: '售'
}
const orderShortcuts = computed(() => orderTabs
  .filter((item) => item.key)
  .map((item) => ({
    ...item,
    count: `${mockOrders.filter((order) => order.status === item.key).length}`,
    icon: orderShortcutIconMap[item.key]
  })))
const benefitItems = [
  { value: '1260', label: '会员积分', desc: '可兑换菜品券' },
  { value: '6', label: '可用优惠券', desc: '满减券与单品券' },
  { value: '¥58', label: '本月节省', desc: '已累计优惠金额' },
  { value: 'VIP', label: '会员等级', desc: '尊享优先出餐' }
]
const serviceItems = [
  { label: '收货地址', desc: '软件园 A 区 2 号楼' },
  { label: '我的优惠券', desc: '3 张可用，2 张即将到期' },
  { label: '联系客服', desc: '在线服务中，平均 30 秒响应' },
  { label: '设置', desc: '支付方式、通知与隐私' }
]

function switchCategory(categoryId) {
  activeCategoryId.value = categoryId
}

function switchTab(tabKey) {
  activeTab.value = tabKey
}

function openOrderList(status = '') {
  const url = status ? `/pages/order/list?status=${status}` : '/pages/order/list'
  uni.navigateTo({
    url
  })
}

function openCart() {
  if (cartPopupRef.value) {
    cartPopupRef.value.open()
  }
}

function getDishCount(dishId) {
  const item = state.items.find((cartItem) => cartItem.id === dishId)
  return item ? item.count : 0
}

function handleAdd(dish) {
  addDish(dish)
}

function handleDecrease(dishId) {
  decreaseDish(dishId)
}

function handlePopupCountChange({ id, value }) {
  const current = getDishCount(id)
  const targetDish = dishCategories
    .reduce((result, category) => result.concat(category.dishes || []), [])
    .find((dish) => dish.id === id)

  if (value > current && targetDish) {
    addDish(targetDish)
    return
  }

  decreaseDish(id)
}

function goCheckout() {
  if (!canCheckout.value) {
    uni.showToast({
      title: `还差 ¥${amountToStart.value.toFixed(0)} 起送`,
      icon: 'none'
    })
    return
  }

  uni.navigateTo({
    url: '/pages/order/confirm'
  })
}

function handleLogout() {
  uni.showToast({
    title: '退出登录 demo',
    icon: 'none'
  })
}
</script>

<style lang="scss" scoped>
.page {
  height: 100vh;
  background: #fffdf8;
  overflow: hidden;
}
</style>
