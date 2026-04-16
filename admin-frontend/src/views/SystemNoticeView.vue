<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">公告管理</h1>
        <p class="page-desc">维护系统内部公告，支持发布状态和排序管理。</p>
      </div>
      <el-button type="primary" @click="openCreateDialog">新增公告</el-button>
    </div>

    <section class="panel-card section-card">
      <el-table :data="rows" width="100%">
        <el-table-column prop="title" label="公告标题" min-width="220" />
        <el-table-column prop="content" label="公告内容" min-width="320" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="90" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" min-width="180" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" :title="dialogMode === 'create' ? '新增公告' : '编辑公告'" width="620px">
      <el-form label-width="92px">
        <el-form-item label="公告标题">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入公告内容" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="发布状态">
          <el-switch v-model="form.statusSwitch" />
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
  createSystemNotice,
  deleteSystemNotice,
  fetchSystemNotices,
  updateSystemNotice
} from '../api/systemNotice'

const rows = ref([])
const dialogVisible = ref(false)
const dialogMode = ref('create')

const form = reactive({
  id: '',
  title: '',
  content: '',
  sort: 0,
  statusSwitch: true
})

function resetForm() {
  form.id = ''
  form.title = ''
  form.content = ''
  form.sort = 0
  form.statusSwitch = true
}

async function loadRows() {
  try {
    rows.value = await fetchSystemNotices()
  } catch (error) {
    ElMessage.error(`公告加载失败：${error.message}`)
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
  form.content = row.content
  form.sort = row.sort
  form.statusSwitch = row.status === 1
  dialogVisible.value = true
}

async function submitForm() {
  const payload = {
    title: form.title,
    content: form.content,
    sort: form.sort,
    status: form.statusSwitch ? 1 : 0
  }

  try {
    if (dialogMode.value === 'create') {
      await createSystemNotice(payload)
      ElMessage.success('公告新增成功')
    } else {
      await updateSystemNotice(form.id, payload)
      ElMessage.success('公告更新成功')
    }
    dialogVisible.value = false
    await loadRows()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除公告“${row.title}”吗？`, '提示', { type: 'warning' })
    await deleteSystemNotice(row.id)
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
