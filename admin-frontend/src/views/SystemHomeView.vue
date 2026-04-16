<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">首页设置</h1>
        <p class="page-desc">维护运营工作台中的卡片指标，新增后会实时展示到首页。</p>
      </div>
      <el-button type="primary" @click="openCreateDialog">新增卡片</el-button>
    </div>

    <section class="panel-card section-card">
      <el-table :data="rows" width="100%">
        <el-table-column prop="title" label="卡片名称" min-width="180" />
        <el-table-column prop="cardValue" label="指标值" min-width="140" />
        <el-table-column prop="extraText" label="补充说明" min-width="220" />
        <el-table-column prop="cardKey" label="唯一标识" min-width="160" />
        <el-table-column prop="sort" label="排序" width="90" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled === 1 ? 'success' : 'info'">
              {{ row.enabled === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" :title="dialogMode === 'create' ? '新增卡片' : '编辑卡片'" width="520px">
      <el-form label-width="92px">
        <el-form-item label="卡片名称">
          <el-input v-model="form.title" placeholder="例如：今日订单" />
        </el-form-item>
        <el-form-item label="指标值">
          <el-input v-model="form.cardValue" placeholder="例如：128" />
        </el-form-item>
        <el-form-item label="补充说明">
          <el-input v-model="form.extraText" placeholder="例如：较昨日 +12%" />
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.cardKey" placeholder="例如：todayOrderCount" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.enabledSwitch" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createDashboardCard,
  deleteDashboardCard,
  fetchDashboardCards,
  updateDashboardCard
} from '../api/dashboardCard'

const rows = ref([])
const dialogVisible = ref(false)
const dialogMode = ref('create')

const form = reactive({
  id: '',
  title: '',
  cardValue: '',
  extraText: '',
  cardKey: '',
  sort: 0,
  enabledSwitch: true
})

function resetForm() {
  form.id = ''
  form.title = ''
  form.cardValue = ''
  form.extraText = ''
  form.cardKey = ''
  form.sort = 0
  form.enabledSwitch = true
}

async function loadRows() {
  try {
    rows.value = await fetchDashboardCards()
  } catch (error) {
    ElMessage.error(`首页设置加载失败：${error.message}`)
  }
}

function openCreateDialog() {
  dialogMode.value = 'create'
  resetForm()
  dialogVisible.value = true
}

function openEditDialog(row) {
  dialogMode.value = 'edit'
  form.id = row.id
  form.title = row.title
  form.cardValue = row.cardValue
  form.extraText = row.extraText
  form.cardKey = row.cardKey
  form.sort = row.sort
  form.enabledSwitch = row.enabled === 1
  dialogVisible.value = true
}

async function submitForm() {
  const payload = {
    title: form.title,
    cardValue: form.cardValue,
    extraText: form.extraText,
    cardKey: form.cardKey,
    sort: form.sort,
    enabled: form.enabledSwitch ? 1 : 0
  }

  try {
    if (dialogMode.value === 'create') {
      await createDashboardCard(payload)
      ElMessage.success('卡片新增成功')
    } else {
      await updateDashboardCard(form.id, payload)
      ElMessage.success('卡片更新成功')
    }
    dialogVisible.value = false
    await loadRows()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除卡片“${row.title}”吗？`, '提示', { type: 'warning' })
    await deleteDashboardCard(row.id)
    ElMessage.success('删除成功')
    await loadRows()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`删除失败：${error.message}`)
    }
  }
}

onMounted(() => {
  loadRows()
})
</script>

<style scoped>
.section-card {
  padding: 20px 22px;
}
</style>
