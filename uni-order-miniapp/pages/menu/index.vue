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
      :key="profileRenderKey"
      :sub-page-style="subPageStyle"
      :top-spacer-height="subPageTopPadding"
      :user-info="miniappUser"
      :is-logged-in="isLoggedIn"
      :order-shortcuts="orderShortcuts"
      :benefit-items="benefitItems"
      :service-items="serviceItems"
      @open-order-list="openOrderList"
      @auth-change="handleAuthChange"
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
import { onLoad, onShow } from '@dcloudio/uni-app'
import BottomTabBar from '@/components/BottomTabBar.vue'
import CartPopup from '@/components/CartPopup.vue'
import MenuContent from '@/components/MenuContent.vue'
import ProfileContent from '@/components/ProfileContent.vue'
import StoreHeader from '@/components/StoreHeader.vue'
import { useCart } from '@/composables/useCart'
import { dishCategories as fallbackDishCategories, shopInfo as fallbackShopInfo } from '@/mock/menu'
import { mockOrders } from '@/mock/orders'
import { getMiniappUser } from '@/services/auth'
import { fetchMenuCategories } from '@/services/menu'

const localDishImages = [
  '/static/images/dish-1.svg',
  '/static/images/dish-2.svg',
  '/static/images/dish-3.svg',
  '/static/images/dish-4.svg',
  '/static/images/dish-5.svg'
]

const shortcutConfigs = [
  { key: 'pendingPay', label: '待付款', icon: '付' },
  { key: 'pickup', label: '待取餐', icon: '取' },
  { key: 'review', label: '待评价', icon: '评' },
  { key: 'refund', label: '退款售后', icon: '售' }
]

const systemInfo = uni.getSystemInfoSync()
const statusBarHeight = systemInfo.statusBarHeight || 24
const menuButtonRect = typeof uni.getMenuButtonBoundingClientRect === 'function'
  ? uni.getMenuButtonBoundingClientRect()
  : null
const capsuleHeight = menuButtonRect?.height || 32
const capsuleTop = menuButtonRect?.top || statusBarHeight + 8
const capsuleBottom = capsuleTop + capsuleHeight
const defaultNavBottom = statusBarHeight + 44
const subPageTopPadding = Math.max(capsuleBottom, defaultNavBottom) + 28

const activeTab = ref('menu')
const dishCategories = ref([])
const activeCategoryId = ref('')
const cartPopupRef = ref(null)
const miniappUser = ref(null)
const profileRenderKey = ref(0)
const shopInfo = { ...fallbackShopInfo }
const { state, addDish, decreaseDish, clearCart, totalCount, totalPrice, amountToStart, canCheckout } = useCart()

const cartItems = computed(() => state.items)
const isLoggedIn = computed(() => !!(miniappUser.value?.userId && miniappUser.value?.openid))
const visibleCategories = computed(() => {
  const currentCategory = dishCategories.value.find((category) => category.id === activeCategoryId.value)
  return currentCategory ? [currentCategory] : []
})
const allDishes = computed(() => dishCategories.value.reduce((result, category) => result.concat(category.dishes || []), []))
const tabItems = computed(() => [
  { key: 'menu', label: '点餐', icon: 'shop' },
  { key: 'profile', label: '我的', icon: 'person' }
])
const subPageStyle = computed(() => ({}))
const orderShortcuts = computed(() => shortcutConfigs.map((item) => ({
  ...item,
  count: isLoggedIn.value ? `${mockOrders.filter((order) => order.status === item.key).length}` : '0'
})))
const benefitItems = computed(() => [
  { value: isLoggedIn.value ? `${miniappUser.value?.points ?? 1260}` : '--', label: '会员积分', desc: '可兑换菜品券' },
  { value: isLoggedIn.value ? '6' : '--', label: '可用优惠券', desc: '满减券与单品券' },
  { value: isLoggedIn.value ? '¥58' : '--', label: '本月节省', desc: '已累计优惠金额' },
  { value: isLoggedIn.value ? (miniappUser.value?.memberLevel || '普通会员') : '--', label: '会员等级', desc: '尊享优先出餐' }
])
const serviceItems = [
  { label: '收货地址', desc: '软件园 A 区 2 号楼' },
  { label: '我的优惠券', desc: '3 张可用，2 张即将到期' },
  { label: '联系客服', desc: '在线服务中，平均 30 秒响应' },
  { label: '设置', desc: '支付方式、通知与隐私' }
]

function resolveDishImage(image, index) {
  if (typeof image === 'string' && image) {
    if (image.startsWith('http://') || image.startsWith('https://') || image.startsWith('/static/images/')) {
      return image
    }
  }
  return localDishImages[index % localDishImages.length]
}

function normalizeMenuCategories(categories = []) {
  return categories.map((category, categoryIndex) => ({
    id: `${category.id}`,
    name: category.name || '未命名分类',
    tag: category.tag || '',
    dishes: (category.dishes || []).map((dish, dishIndex) => ({
      id: `${dish.id}`,
      categoryId: `${dish.categoryId || category.id}`,
      name: dish.name || '未命名菜品',
      price: Number(dish.price || 0),
      monthlySales: Number(dish.monthlySales || 0),
      praise: Number(dish.praise || 0),
      extra: dish.extra || '',
      image: resolveDishImage(dish.image, categoryIndex + dishIndex)
    }))
  }))
}

function setDishCategories(categories = []) {
  dishCategories.value = normalizeMenuCategories(categories)
  if (!dishCategories.value.length) {
    activeCategoryId.value = ''
    return
  }

  const hasActiveCategory = dishCategories.value.some((category) => category.id === activeCategoryId.value)
  if (!hasActiveCategory) {
    activeCategoryId.value = dishCategories.value[0].id
  }
}

async function loadMenuData() {
  try {
    const categoryData = await fetchMenuCategories()
    if (!Array.isArray(categoryData)) {
      throw new Error('菜单数据格式错误')
    }
    setDishCategories(categoryData)
    if (!categoryData.length) {
      uni.showToast({
        title: '暂无菜单数据',
        icon: 'none'
      })
    }
  } catch (error) {
    setDishCategories(fallbackDishCategories)
    uni.showToast({
      title: '菜单加载失败，已使用演示数据',
      icon: 'none'
    })
  }
}

function refreshMiniappUser(nextUser) {
  const latestUser = nextUser === undefined ? getMiniappUser() : nextUser
  miniappUser.value = latestUser ? { ...latestUser } : null
  profileRenderKey.value += 1
}

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
  const targetDish = allDishes.value.find((dish) => dish.id === id)

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

function handleAuthChange(payload = {}) {
  if (payload.type === 'login') {
    refreshMiniappUser(payload.user || getMiniappUser())
    uni.showToast({
      title: payload.message || '登录成功',
      icon: 'success'
    })
    return
  }

  if (payload.type === 'logout') {
    refreshMiniappUser(null)
    uni.showToast({
      title: payload.message || '已退出登录',
      icon: 'none'
    })
    return
  }

  uni.showToast({
    title: payload.message || '操作失败',
    icon: 'none'
  })
}

onLoad(() => {
  setDishCategories(fallbackDishCategories)
  refreshMiniappUser()
  loadMenuData()
})

onShow(() => {
  refreshMiniappUser()
})
</script>

<style lang="scss" scoped>
.page {
  height: 100vh;
  background: #fffdf8;
  overflow: hidden;
}
</style>
