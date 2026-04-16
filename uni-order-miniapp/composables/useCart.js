import { computed, reactive } from 'vue'

const state = reactive({
  items: []
})

function findItem(dishId) {
  return state.items.find((item) => item.id === dishId)
}

function addDish(dish) {
  const item = findItem(dish.id)
  if (item) {
    item.count += 1
    return
  }

  state.items.push({
    ...dish,
    count: 1
  })
}

function decreaseDish(dishId) {
  const item = findItem(dishId)
  if (!item) return

  item.count -= 1
  if (item.count <= 0) {
    const index = state.items.findIndex((dish) => dish.id === dishId)
    if (index !== -1) {
      state.items.splice(index, 1)
    }
  }
}

function clearCart() {
  state.items.splice(0, state.items.length)
}

const totalCount = computed(() => state.items.reduce((sum, item) => sum + item.count, 0))
const totalPrice = computed(() => state.items.reduce((sum, item) => sum + item.count * item.price, 0))
const amountToStart = computed(() => 0)
const canCheckout = computed(() => totalCount.value > 0)

export function useCart() {
  return {
    state,
    addDish,
    decreaseDish,
    clearCart,
    totalCount,
    totalPrice,
    amountToStart,
    canCheckout
  }
}
