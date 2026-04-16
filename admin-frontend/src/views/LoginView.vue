<template>
  <div class="login-page" :style="{ background: loginBackground }">
    <div class="login-card panel-card">
      <div class="login-brand">
        <div class="login-badge">
          <img v-if="config.logoUrl" :src="config.logoUrl" alt="logo" />
          <span v-else>{{ brandInitial }}</span>
        </div>
        <div>
          <h1>{{ config.systemName || 'Kitchen Admin' }}</h1>
          <p>{{ config.systemIntro || '私人厨房后台管理' }}</p>
        </div>
      </div>

      <div class="welcome-text">{{ config.welcomeText || '欢迎使用私人厨房后台管理系统' }}</div>

      <el-form label-position="top" class="login-form" @submit.prevent="handleLogin">
        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="请输入管理员账号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-button type="primary" size="large" class="login-btn" :loading="submitting" @click="handleLogin">
          登录后台
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/auth'
import { fetchPersonalization } from '../api/personalization'
import { saveLoginUser } from '../utils/auth'
import { applyGlobalThemeColor, getDefaultThemeColor } from '../utils/appTheme'

const router = useRouter()
const submitting = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const config = reactive({
  logoUrl: '',
  systemName: 'Kitchen Admin',
  systemIntro: '私人厨房后台管理',
  welcomeText: '欢迎使用私人厨房后台管理系统',
  loginBackgroundImage: '',
  themeColor: getDefaultThemeColor()
})

const brandInitial = computed(() => {
  return (config.systemName || 'K').slice(0, 1).toUpperCase()
})

const loginBackground = computed(() => {
  if (config.loginBackgroundImage) {
    return `linear-gradient(rgba(15, 23, 42, 0.46), rgba(15, 23, 42, 0.28)), url(${config.loginBackgroundImage}) center / cover no-repeat`
  }
  return 'radial-gradient(circle at top, rgba(255, 215, 90, 0.38), transparent 28%), linear-gradient(180deg, #fff9ea 0%, #f5eee3 100%)'
})

async function loadPersonalization() {
  try {
    const data = await fetchPersonalization()
    config.logoUrl = data?.logoUrl || ''
    config.systemName = data?.systemName || 'Kitchen Admin'
    config.systemIntro = data?.systemIntro || '私人厨房后台管理'
    config.welcomeText = data?.welcomeText || '欢迎使用私人厨房后台管理系统'
    config.loginBackgroundImage = data?.loginBackgroundImage || ''
    config.themeColor = data?.themeColor || getDefaultThemeColor()
    applyGlobalThemeColor(config.themeColor)
  } catch (error) {
    console.error('login personalization load failed', error)
  }
}

async function handleLogin() {
  if (!form.username.trim() || !form.password.trim()) {
    ElMessage.warning('请输入账号和密码')
    return
  }

  submitting.value = true
  try {
    const result = await login({
      username: form.username.trim(),
      password: form.password
    })
    saveLoginUser(result)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    ElMessage.error(`登录失败：${error.message}`)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadPersonalization()
})
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.login-card {
  width: 500px;
  padding: 42px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(10px);
}

.login-brand {
  display: flex;
  align-items: center;
  gap: 16px;
}

.login-badge {
  width: 68px;
  height: 68px;
  border-radius: 22px;
  background: linear-gradient(135deg, var(--app-theme-color), color-mix(in srgb, var(--app-theme-color) 64%, #ffffff));
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  font-weight: 800;
  overflow: hidden;
  flex: none;
}

.login-badge img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

h1 {
  margin: 0;
  font-size: 30px;
  color: #412e17;
}

p {
  margin: 8px 0 0;
  color: #8f7756;
}

.welcome-text {
  margin: 24px 0 28px;
  color: #5a4b3b;
  font-size: 16px;
  line-height: 1.7;
}

.login-btn {
  width: 100%;
  margin-top: 8px;
}
</style>
