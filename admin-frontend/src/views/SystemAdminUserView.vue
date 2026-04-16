<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">后台用户</h1>
        <p class="page-desc">维护后台系统登录用户，当前先支持基础账号信息与状态管理。</p>
      </div>
      <el-button type="primary" @click="openCreateDialog">新增后台用户</el-button>
    </div>

    <section class="panel-card table-card">
      <el-table v-loading="loading" :data="rows" style="width: 100%">
        <el-table-column prop="username" label="账号" min-width="150" />
        <el-table-column prop="displayName" label="显示名称" min-width="150" />
        <el-table-column prop="phone" label="手机号" min-width="150" />
        <el-table-column prop="roleName" label="角色" min-width="120" />
        <el-table-column prop="statusText" label="状态" min-width="100" />
        <el-table-column prop="updateTimeText" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑后台用户' : '新增后台用户'" width="560px">
      <el-form label-position="top">
        <el-form-item label="账号">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="显示名称">
          <el-input v-model="form.displayName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="头像地址">
          <el-input v-model="form.avatar" />
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="form.roleName" />
        </el-form-item>
        <el-form-item label="登录密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
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
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createSysUser,
  deleteSysUser,
  fetchSysUserList,
  updateSysUser
} from '../api/sysAdminUser'

const loading = ref(false)
const rows = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref('')

const form = reactive({
  username: '',
  displayName: '',
  phone: '',
  avatar: '',
  roleName: '超级管理员',
  password: '',
  remark: '',
  status: 1
})

function resetForm() {
  editingId.value = ''
  form.username = ''
  form.displayName = ''
  form.phone = ''
  form.avatar = ''
  form.roleName = '超级管理员'
  form.password = ''
  form.remark = ''
  form.status = 1
}

async function loadRows() {
  loading.value = true
  try {
    const data = await fetchSysUserList()
    rows.value = data.map((item) => ({
      ...item,
      statusText: Number(item.status) === 1 ? '启用' : '停用',
      updateTimeText: item.updateTime ? item.updateTime.replace('T', ' ') : '-'
    }))
  } catch (error) {
    ElMessage.error(`后台用户加载失败：${error.message}`)
  } finally {
    loading.value = false
  }
}

function openCreateDialog() {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

function openEditDialog(row) {
  isEdit.value = true
  editingId.value = row.id
  form.username = row.username
  form.displayName = row.displayName
  form.phone = row.phone || ''
  form.avatar = row.avatar || ''
  form.roleName = row.roleName || '超级管理员'
  form.password = row.password || ''
  form.remark = row.remark || ''
  form.status = row.status
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    const payload = { ...form }
    if (isEdit.value) {
      await updateSysUser(editingId.value, payload)
      ElMessage.success('后台用户更新成功')
    } else {
      await createSysUser(payload)
      ElMessage.success('后台用户新增成功')
    }
    dialogVisible.value = false
    await loadRows()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除后台用户“${row.displayName || row.username}”吗？`, '删除确认', {
      type: 'warning'
    })
    await deleteSysUser(row.id)
    ElMessage.success('后台用户删除成功')
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
.table-card {
  padding: 18px;
}
</style>
