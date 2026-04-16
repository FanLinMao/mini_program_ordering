<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">用户管理</h1>
        <p class="page-desc">沉淀用户资料、积分、会员等级和消费行为，为后续运营做准备。</p>
      </div>
      <el-button type="warning" @click="openCreateDialog">新增用户</el-button>
    </div>

    <section class="panel-card table-card">
      <div class="table-wrap">
        <el-table v-loading="loading" :data="rows" style="width: 100%">
          <el-table-column prop="nickname" label="昵称" min-width="160" />
          <el-table-column prop="phone" label="手机号" min-width="160" />
          <el-table-column prop="points" label="积分" min-width="120" />
          <el-table-column prop="memberLevel" label="会员等级" min-width="120" />
          <el-table-column prop="statusText" label="状态" min-width="120" />
          <el-table-column prop="updateTimeText" label="更新时间" min-width="180" />
          <el-table-column label="操作" width="220" fixed="right" align="right" header-align="center">
            <template #default="{ row }">
              <div class="action-cell">
                <el-button link type="warning" @click="openEditDialog(row)">编辑</el-button>
                <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-if="!loading && rows.length === 0" class="empty-tip">
        当前暂无用户数据
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="560px">
      <el-form label-position="top">
        <el-form-item label="OpenID">
          <el-input v-model="form.openid" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="头像地址">
          <el-input v-model="form.avatar" />
        </el-form-item>
        <el-form-item label="积分">
          <el-input-number v-model="form.points" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="会员等级">
          <el-input v-model="form.memberLevel" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
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
import { createUser, deleteUser, fetchUserList, updateUser } from '../api/user'

const loading = ref(false)
const rows = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const form = reactive({
  openid: '',
  nickname: '',
  phone: '',
  avatar: '',
  points: 0,
  memberLevel: '普通',
  status: 1
})

const resetForm = () => {
  editingId.value = null
  form.openid = ''
  form.nickname = ''
  form.phone = ''
  form.avatar = ''
  form.points = 0
  form.memberLevel = '普通'
  form.status = 1
}

const loadUsers = async () => {
  loading.value = true
  try {
    const data = await fetchUserList()
    rows.value = data.map((item) => ({
      ...item,
      statusText: Number(item.status) === 1 ? '正常' : '禁用',
      updateTimeText: item.updateTime ? item.updateTime.replace('T', ' ') : '-'
    }))
  } catch (error) {
    ElMessage.error(`用户数据加载失败：${error.message}`)
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
  form.openid = row.openid
  form.nickname = row.nickname
  form.phone = row.phone
  form.avatar = row.avatar || ''
  form.points = row.points || 0
  form.memberLevel = row.memberLevel || '普通'
  form.status = row.status
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    const payload = { ...form }
    if (isEdit.value) {
      await updateUser(editingId.value, payload)
      ElMessage.success('用户更新成功')
    } else {
      await createUser(payload)
      ElMessage.success('用户新增成功')
    }
    dialogVisible.value = false
    await loadUsers()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除用户“${row.nickname}”吗？`, '删除确认', {
      type: 'warning'
    })
    await deleteUser(row.id)
    ElMessage.success('用户删除成功')
    await loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`删除失败：${error.message}`)
    }
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.table-card {
  padding: 18px;
}

.table-wrap {
  width: 100%;
}

.table-wrap :deep(.el-table) {
  width: 100% !important;
}

.table-wrap :deep(.el-table__inner-wrapper) {
  width: 100%;
}

.action-cell {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.empty-tip {
  padding: 18px 0 4px;
  color: #8a96aa;
  font-size: 14px;
  text-align: center;
}
</style>
