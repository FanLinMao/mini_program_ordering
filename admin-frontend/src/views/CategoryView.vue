<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">分类管理</h1>
        <p class="page-desc">维护点餐页左侧分类，支持排序、启停和展示控制。</p>
      </div>
      <el-button type="warning" @click="openCreateDialog">新增分类</el-button>
    </div>

    <section class="panel-card table-card">
      <el-table v-loading="loading" :data="rows" style="width: 100%">
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sort" label="排序" width="120" />
        <el-table-column prop="statusText" label="状态" width="120" />
        <el-table-column prop="updateBy" label="最后更新人" width="140" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="220" header-align="center">
          <template #default="{ row }">
            <el-button link type="warning" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="480px">
      <el-form label-position="top">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="warning" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createCategory,
  deleteCategory,
  fetchCategoryList,
  updateCategory
} from '../api/category'

const loading = ref(false)
const rows = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const form = reactive({
  name: '',
  sort: 0,
  status: 1
})

const resetForm = () => {
  form.name = ''
  form.sort = 0
  form.status = 1
  editingId.value = null
}

const mapRows = (data) => {
  rows.value = data.map((item) => ({
    ...item,
    statusText: Number(item.status) === 1 ? '启用' : '停用',
    updateBy: item.updateBy || 'system',
    updateTime: item.updateTime ? item.updateTime.replace('T', ' ') : '-'
  }))
}

const loadCategories = async () => {
  loading.value = true
  try {
    mapRows(await fetchCategoryList())
  } catch (error) {
    ElMessage.error(`分类数据加载失败：${error.message}`)
  } finally {
    loading.value = false
  }
}

const openCreateDialog = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  isEdit.value = true
  editingId.value = row.id
  form.name = row.name
  form.sort = row.sort
  form.status = row.status
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    const payload = {
      name: form.name,
      sort: form.sort,
      status: form.status
    }

    if (isEdit.value) {
      await updateCategory(editingId.value, payload)
      ElMessage.success('分类更新成功')
    } else {
      await createCategory(payload)
      ElMessage.success('分类新增成功')
    }

    dialogVisible.value = false
    await loadCategories()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除分类“${row.name}”吗？`, '删除确认', {
      type: 'warning'
    })
    await deleteCategory(row.id)
    ElMessage.success('分类删除成功')
    await loadCategories()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`删除失败：${error.message}`)
    }
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.table-card {
  padding: 18px;
}
</style>
