<template>
  <view v-if="visible" class="popup-mask" @tap="close">
    <view class="popup-panel" :class="type" @tap.stop>
      <slot />
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  type: {
    type: String,
    default: 'center'
  }
})

const visible = ref(false)

function open() {
  visible.value = true
}

function close() {
  visible.value = false
}

defineExpose({
  open,
  close
})
</script>

<style lang="scss" scoped>
.popup-mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.32);
  z-index: 999;
}

.popup-panel {
  position: absolute;
  width: 100%;
}

.popup-panel.bottom {
  left: 0;
  right: 0;
  bottom: 0;
}
</style>
