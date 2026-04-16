<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">运营工作台</h1>
        <p class="page-desc">先用原型把关键指标和处理入口建立起来，后面再接真实接口。</p>
      </div>
    </div>

    <div class="metric-grid">
      <div v-for="item in metrics" :key="item.label" class="metric-card panel-card">
        <div class="metric-label">{{ item.label }}</div>
        <div class="metric-value">{{ item.value }}</div>
        <div class="metric-extra">{{ item.extra }}</div>
      </div>
    </div>

    <div class="overview-grid">
      <section class="panel-card section-card">
        <div class="section-title">待处理事项</div>
        <div v-for="task in tasks" :key="task.title" class="todo-item">
          <div>
            <div class="todo-title">{{ task.title }}</div>
            <div class="todo-desc">{{ task.desc }}</div>
          </div>
          <el-tag type="warning" effect="plain">{{ task.count }}</el-tag>
        </div>
      </section>

      <section class="panel-card section-card">
        <div class="section-title">系统建议</div>
        <div v-for="tip in tips" :key="tip" class="tip-item">{{ tip }}</div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchDashboardOverview } from '../api/dashboard'

const metrics = ref([])
const tasks = ref([])
const tips = ref([])

const loadDashboard = async () => {
  try {
    const data = await fetchDashboardOverview()
    metrics.value = data.metrics || []
    tasks.value = data.tasks || []
    tips.value = data.tips || []
  } catch (error) {
    ElMessage.error(`工作台数据加载失败：${error.message}`)
  }
}

onMounted(() => {
  loadDashboard()
})
</script>

<style scoped>
.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.metric-card,
.section-card {
  padding: 22px;
}

.metric-label,
.section-title {
  font-size: 15px;
  color: #8d7657;
}

.metric-value {
  margin-top: 12px;
  font-size: 34px;
  font-weight: 800;
  color: #452f15;
}

.metric-extra {
  margin-top: 8px;
  color: #b07d1b;
  font-size: 13px;
}

.overview-grid {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 18px;
}

.todo-item,
.tip-item {
  padding: 16px 0;
  border-top: 1px solid rgba(130, 96, 42, 0.08);
}

.todo-item:first-of-type,
.tip-item:first-of-type {
  margin-top: 8px;
}

.todo-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.todo-title {
  font-size: 16px;
  font-weight: 700;
  color: #463117;
}

.todo-desc,
.tip-item {
  color: #8f7759;
  font-size: 14px;
}
</style>
