<template>
  <text class="uni-icons" :style="iconStyle">{{ currentIcon }}</text>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: ''
  },
  size: {
    type: [String, Number],
    default: 16
  },
  color: {
    type: String,
    default: '#333333'
  }
})

const iconMap = {
  left: '‹',
  search: '⌕',
  'more-filled': '⋯',
  plus: '+',
  minus: '−',
  cart: '🛒',
  shop: '◫',
  person: '◉',
  'wallet-filled': '￥',
  'star-filled': '★',
  reload: '↺'
}

const currentIcon = computed(() => iconMap[props.type] || '•')
const iconStyle = computed(() => ({
  color: props.color,
  fontSize: `${props.size}px`,
  lineHeight: 1
}))
</script>

<style scoped>
.uni-icons {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
</style>
