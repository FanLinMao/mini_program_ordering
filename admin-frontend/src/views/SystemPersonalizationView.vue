<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">个性化</h1>
        <p class="page-desc">统一维护后台品牌展示信息、背景图片与按钮主题色。</p>
      </div>
      <el-button type="primary" :loading="saving" @click="handleSave">保存设置</el-button>
    </div>

    <section class="panel-card section-card">
      <el-form label-width="116px" class="config-form">
        <el-form-item label="系统 Logo">
          <el-input v-model="form.logoUrl" placeholder="请输入 Logo 图片地址" />
        </el-form-item>

        <el-form-item label="系统名称">
          <el-input v-model="form.systemName" placeholder="例如：Kitchen Admin" />
        </el-form-item>

        <el-form-item label="系统简介">
          <el-input v-model="form.systemIntro" placeholder="例如：私人厨房后台管理" />
        </el-form-item>

        <el-form-item label="欢迎语">
          <el-input
            v-model="form.welcomeText"
            placeholder="例如：欢迎使用私人厨房后台管理系统"
          />
        </el-form-item>

        <el-form-item label="系统背景图">
          <el-input v-model="form.systemBackgroundImage" placeholder="请输入系统背景图地址" />
        </el-form-item>

        <el-form-item label="登录页背景图">
          <el-input v-model="form.loginBackgroundImage" placeholder="请输入登录页背景图地址" />
        </el-form-item>

        <el-form-item label="按钮主题颜色">
          <div class="color-row">
            <el-color-picker v-model="form.themeColor" />
            <el-input v-model="form.themeColor" placeholder="例如：#409EFF" />
          </div>
        </el-form-item>
      </el-form>

      <div class="preview-card">
        <div class="preview-header">
          <div class="preview-logo">
            <img v-if="form.logoUrl" :src="form.logoUrl" alt="logo" />
            <span v-else>{{ (form.systemName || 'K').slice(0, 1) }}</span>
          </div>
          <div>
            <div class="preview-title">{{ form.systemName || 'Kitchen Admin' }}</div>
            <div class="preview-subtitle">{{ form.systemIntro || '私人厨房后台管理' }}</div>
          </div>
        </div>
        <div class="preview-banner" :style="{ background: previewBackground }">
          <div class="preview-text">{{ form.welcomeText || '欢迎使用私人厨房后台管理系统' }}</div>
          <button class="preview-button" :style="{ background: form.themeColor || '#409EFF' }">
            主题按钮预览
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchPersonalization, savePersonalization } from '../api/personalization'
import {
  applyGlobalBackgroundImage,
  applyGlobalThemeColor,
  getDefaultThemeColor
} from '../utils/appTheme'

const saving = ref(false)

const form = reactive({
  id: '',
  logoUrl: '',
  systemName: '',
  systemIntro: '',
  welcomeText: '',
  systemBackgroundImage: '',
  loginBackgroundImage: '',
  themeColor: getDefaultThemeColor()
})

const previewBackground = computed(() => {
  if (form.systemBackgroundImage) {
    return `linear-gradient(rgba(19, 33, 56, 0.38), rgba(19, 33, 56, 0.38)), url(${form.systemBackgroundImage}) center / cover`
  }
  return 'linear-gradient(135deg, #213547 0%, #385d8a 100%)'
})

function applyForm(data = {}) {
  form.id = data.id || ''
  form.logoUrl = data.logoUrl || ''
  form.systemName = data.systemName || 'Kitchen Admin'
  form.systemIntro = data.systemIntro || '私人厨房后台管理'
  form.welcomeText = data.welcomeText || '欢迎使用私人厨房后台管理系统'
  form.systemBackgroundImage = data.systemBackgroundImage || ''
  form.loginBackgroundImage = data.loginBackgroundImage || ''
  form.themeColor = data.themeColor || getDefaultThemeColor()
  applyGlobalThemeColor(form.themeColor)
  applyGlobalBackgroundImage(form.systemBackgroundImage)
}

async function loadConfig() {
  try {
    const data = await fetchPersonalization()
    applyForm(data)
  } catch (error) {
    ElMessage.error(`个性化配置加载失败：${error.message}`)
  }
}

async function handleSave() {
  saving.value = true
  try {
    const result = await savePersonalization({ ...form })
    applyForm(result)
    applyGlobalThemeColor(form.themeColor)
    applyGlobalBackgroundImage(form.systemBackgroundImage)
    window.dispatchEvent(
      new CustomEvent('admin-personalization-changed', {
        detail: {
          ...result,
          themeColor: form.themeColor,
          systemBackgroundImage: form.systemBackgroundImage
        }
      })
    )
    ElMessage.success('个性化配置保存成功')
  } catch (error) {
    ElMessage.error(`个性化配置保存失败：${error.message}`)
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.section-card {
  padding: 24px;
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(320px, 0.9fr);
  gap: 22px;
}

.config-form {
  min-width: 0;
}

.color-row {
  width: 100%;
  display: grid;
  grid-template-columns: 52px 1fr;
  gap: 12px;
  align-items: center;
}

.preview-card {
  padding: 20px;
  border: 1px solid #e8edf5;
  border-radius: 20px;
  background: #fbfdff;
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 18px;
}

.preview-logo {
  width: 54px;
  height: 54px;
  border-radius: 16px;
  background: #1f3a5f;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  font-weight: 700;
  overflow: hidden;
}

.preview-logo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-title {
  font-size: 18px;
  font-weight: 700;
  color: #243142;
}

.preview-subtitle {
  margin-top: 6px;
  font-size: 13px;
  color: #7a8799;
}

.preview-banner {
  min-height: 240px;
  padding: 28px 24px;
  border-radius: 18px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: #ffffff;
}

.preview-text {
  max-width: 280px;
  font-size: 24px;
  line-height: 1.5;
  font-weight: 700;
}

.preview-button {
  width: 140px;
  height: 42px;
  border: 0;
  border-radius: 999px;
  color: #ffffff;
  font-size: 14px;
  font-weight: 600;
}

@media (max-width: 1200px) {
  .section-card {
    grid-template-columns: 1fr;
  }
}
</style>
