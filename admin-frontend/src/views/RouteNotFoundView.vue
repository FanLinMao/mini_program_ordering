<template>
  <div class="page-shell">
    <section class="panel-card not-found-card">
      <div class="status-code">404</div>
      <h1 class="status-title">页面未找到</h1>
      <p class="status-desc">
        当前菜单已成功打开，但绑定的页面组件不存在或尚未接入前端项目。
      </p>
      <p v-if="missingComponent" class="status-meta">
        当前配置的组件路径：`{{ missingComponent }}`
      </p>
      <p v-if="currentPath" class="status-meta">
        当前访问路径：`{{ currentPath }}`
      </p>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const missingComponent = computed(() => route.meta?.missingComponent || route.meta?.component || '')
const currentPath = computed(() => route.path)
</script>

<style scoped>
.not-found-card {
  min-height: 360px;
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.status-code {
  font-size: 64px;
  line-height: 1;
  font-weight: 800;
  color: #d58b1f;
}

.status-title {
  margin: 16px 0 8px;
  font-size: 28px;
  color: #342515;
}

.status-desc {
  margin: 0;
  font-size: 15px;
  color: #7e6b54;
}

.status-meta {
  margin: 12px 0 0;
  font-size: 13px;
  color: #9a8871;
}
</style>
