<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">个人中心</h1>
        <p class="page-desc">维护当前后台账号的基础资料、头像、登录密码，并查看最近操作日志。</p>
      </div>
      <div class="page-actions">
        <el-button @click="loadAllData">刷新数据</el-button>
      </div>
    </div>

    <div class="profile-grid">
      <section class="panel-card profile-card">
        <div class="profile-head">
          <div class="profile-avatar">
            <img v-if="form.avatar" :src="form.avatar" alt="avatar" />
            <span v-else>{{ userInitial }}</span>
          </div>

          <div class="profile-summary">
            <div class="profile-name">{{ displayName }}</div>
            <div class="profile-role">
              <el-tag size="small" effect="plain">{{ profile.roleName || '系统管理员' }}</el-tag>
            </div>
            <div class="profile-account">登录账号：{{ profile.username || '-' }}</div>
          </div>
        </div>

        <el-form label-position="top" class="profile-form">
          <div class="form-grid">
            <el-form-item label="系统账号">
              <el-input :model-value="profile.username || '-'" disabled />
            </el-form-item>

            <el-form-item label="状态">
              <el-input :model-value="profile.status === 1 ? '正常' : '停用'" disabled />
            </el-form-item>

            <el-form-item label="显示名称">
              <el-input v-model="form.displayName" placeholder="请输入显示名称" />
            </el-form-item>

            <el-form-item label="手机号码">
              <el-input v-model="form.phone" placeholder="请输入手机号码" />
            </el-form-item>
          </div>

          <el-form-item label="头像地址">
            <el-input v-model="form.avatar" placeholder="请输入头像图片地址" />
          </el-form-item>

          <el-form-item label="备注信息">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="3"
              resize="none"
              placeholder="请输入备注信息"
            />
          </el-form-item>
        </el-form>

        <div class="action-row">
          <el-button type="primary" :loading="savingProfile" @click="saveProfile">保存资料</el-button>
          <el-button @click="openPasswordDialog">修改密码</el-button>
        </div>
      </section>

      <section class="panel-card side-card">
        <div class="side-card-title">账号概览</div>

        <div class="info-list">
          <div class="info-row">
            <span class="info-label">角色名称</span>
            <span class="info-value">{{ profile.roleName || '系统管理员' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">账号状态</span>
            <span class="info-value">{{ profile.status === 1 ? '正常' : '停用' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">最后更新人</span>
            <span class="info-value">{{ profile.updateBy || '-' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">最后更新时间</span>
            <span class="info-value">{{ profile.updateTime || '-' }}</span>
          </div>
        </div>

        <div class="avatar-preview">
          <div class="avatar-preview-title">头像预览</div>
          <div class="avatar-preview-box">
            <img v-if="form.avatar" :src="form.avatar" alt="avatar-preview" />
            <span v-else>{{ userInitial }}</span>
          </div>
        </div>
      </section>
    </div>

    <section class="panel-card log-card">
      <div class="log-head">
        <div>
          <div class="side-card-title">最近操作日志</div>
          <div class="log-desc">展示当前账号最近 20 条登录、退出、资料修改与密码变更记录。</div>
        </div>
        <el-button :loading="logsLoading" @click="loadLoginLogs">刷新日志</el-button>
      </div>

      <el-table v-if="loginLogs.length" :data="loginLogs" border stripe>
        <el-table-column label="操作类型" min-width="140">
          <template #default="{ row }">
            {{ actionLabelMap[row.actionType] || row.actionType || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="结果" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.successFlag === 1 ? 'success' : 'danger'">
              {{ row.successFlag === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP 地址" min-width="140" />
        <el-table-column prop="remark" label="说明" min-width="180" />
        <el-table-column prop="createTime" label="时间" min-width="180" />
      </el-table>

      <el-empty v-else description="暂无日志记录" />
    </section>

    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="420px" destroy-on-close>
      <el-form label-position="top">
        <el-form-item label="原密码">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入新密码"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="savingPassword" @click="savePassword">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  changeCurrentUserPassword,
  fetchCurrentUser,
  fetchCurrentUserLoginLogs,
  updateCurrentUserProfile
} from '../api/currentUser'
import { getLoginUser, updateLoginUser } from '../utils/auth'

const actionLabelMap = {
  LOGIN: '登录系统',
  LOGOUT: '退出系统',
  UPDATE_PROFILE: '修改资料',
  CHANGE_PASSWORD: '修改密码'
}

const currentLoginUser = getLoginUser() || {}
const username = currentLoginUser.username || 'admin'

const savingProfile = ref(false)
const savingPassword = ref(false)
const logsLoading = ref(false)
const passwordDialogVisible = ref(false)
const loginLogs = ref([])

const profile = reactive({
  username,
  displayName: '',
  phone: '',
  avatar: '',
  roleName: '',
  status: 1,
  remark: '',
  updateTime: '',
  updateBy: ''
})

const form = reactive({
  displayName: '',
  phone: '',
  avatar: '',
  remark: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const displayName = computed(() => profile.displayName || profile.username || 'Admin')
const userInitial = computed(() => displayName.value.slice(0, 1).toUpperCase())

function fillProfile(data = {}) {
  profile.username = data.username || username
  profile.displayName = data.displayName || ''
  profile.phone = data.phone || ''
  profile.avatar = data.avatar || ''
  profile.roleName = data.roleName || ''
  profile.status = data.status ?? 1
  profile.remark = data.remark || ''
  profile.updateTime = data.updateTime || ''
  profile.updateBy = data.updateBy || ''

  form.displayName = profile.displayName
  form.phone = profile.phone
  form.avatar = profile.avatar
  form.remark = profile.remark
}

async function loadProfile() {
  const data = await fetchCurrentUser(username)
  fillProfile(data || {})
}

async function loadLoginLogs() {
  logsLoading.value = true
  try {
    loginLogs.value = await fetchCurrentUserLoginLogs(username)
  } finally {
    logsLoading.value = false
  }
}

async function loadAllData() {
  try {
    await Promise.all([loadProfile(), loadLoginLogs()])
  } catch (error) {
    ElMessage.error(error.message || '个人中心数据加载失败')
  }
}

async function saveProfile() {
  savingProfile.value = true
  try {
    const updated = await updateCurrentUserProfile(username, { ...form })
    fillProfile(updated || {})
    updateLoginUser({
      displayName: profile.displayName,
      avatar: profile.avatar,
      roleName: profile.roleName,
      status: profile.status
    })
    ElMessage.success('个人资料已更新')
    await loadLoginLogs()
  } catch (error) {
    ElMessage.error(error.message || '个人资料更新失败')
  } finally {
    savingProfile.value = false
  }
}

function openPasswordDialog() {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

async function savePassword() {
  if (!passwordForm.oldPassword || !passwordForm.newPassword || !passwordForm.confirmPassword) {
    ElMessage.warning('请完整填写密码信息')
    return
  }

  if (passwordForm.newPassword.length < 6) {
    ElMessage.warning('新密码长度不能少于 6 位')
    return
  }

  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次输入的新密码不一致')
    return
  }

  savingPassword.value = true
  try {
    await changeCurrentUserPassword(username, {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    passwordDialogVisible.value = false
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
    ElMessage.success('密码修改成功')
    await loadLoginLogs()
  } catch (error) {
    ElMessage.error(error.message || '密码修改失败')
  } finally {
    savingPassword.value = false
  }
}

onMounted(() => {
  loadAllData()
})
</script>

<style scoped>
.page-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.profile-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(320px, 0.8fr);
  gap: 20px;
}

.profile-card,
.side-card,
.log-card {
  padding: 24px;
}

.profile-head {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 24px;
}

.profile-avatar {
  width: 80px;
  height: 80px;
  border-radius: 24px;
  background: linear-gradient(
    135deg,
    var(--app-theme-color),
    color-mix(in srgb, var(--app-theme-color) 60%, #ffffff)
  );
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  font-weight: 800;
  overflow: hidden;
  flex: none;
}

.profile-avatar img,
.avatar-preview-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-summary {
  min-width: 0;
}

.profile-name {
  color: #2f3b52;
  font-size: 24px;
  font-weight: 700;
}

.profile-role,
.profile-account,
.log-desc {
  margin-top: 8px;
  color: #7a889e;
  font-size: 14px;
}

.profile-form {
  margin-top: 10px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.action-row {
  display: flex;
  gap: 12px;
  margin-top: 10px;
}

.side-card-title {
  color: #2f3b52;
  font-size: 18px;
  font-weight: 700;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-top: 18px;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #edf2f7;
}

.info-label {
  color: #7f8ca0;
  font-size: 14px;
}

.info-value {
  color: #2f3b52;
  font-size: 15px;
  font-weight: 600;
  text-align: right;
}

.avatar-preview {
  margin-top: 24px;
}

.avatar-preview-title {
  color: #2f3b52;
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 14px;
}

.avatar-preview-box {
  width: 96px;
  height: 96px;
  border-radius: 24px;
  background: #eef3f9;
  color: #5a6980;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 34px;
  font-weight: 800;
  overflow: hidden;
}

.log-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 18px;
}

@media (max-width: 1180px) {
  .profile-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .profile-head,
  .log-head,
  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
