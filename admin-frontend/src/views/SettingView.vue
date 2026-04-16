<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">店铺设置</h1>
        <p class="page-desc">集中维护门店信息、营业时间、公告和支付开关。</p>
      </div>
      <el-button type="warning" :loading="saving" @click="handleSave">保存设置</el-button>
    </div>

    <div class="setting-grid">
      <section class="panel-card section-card">
        <div class="section-title">基础信息</div>
        <el-form label-position="top">
          <el-form-item label="店铺名称">
            <el-input v-model="form.shopName" />
          </el-form-item>
          <el-form-item label="门店地址">
            <el-input v-model="form.address" />
          </el-form-item>
          <el-form-item label="营业时间">
            <el-input v-model="form.businessHours" />
          </el-form-item>
          <el-form-item label="店铺公告">
            <el-input v-model="form.notice" type="textarea" :rows="4" />
          </el-form-item>
        </el-form>
      </section>

      <section class="panel-card section-card">
        <div class="section-title">业务开关</div>
        <div class="switch-row">
          <span>支持到店自取</span>
          <el-switch v-model="takeoutSwitch" />
        </div>
        <div class="switch-row">
          <span>支持现炒下单</span>
          <el-switch v-model="cookNowSwitch" />
        </div>
        <div class="switch-row">
          <span>支持微信支付</span>
          <el-switch v-model="wxpaySwitch" />
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchCurrentShopSetting, saveShopSetting } from '../api/shopSetting'

const saving = ref(false)
const form = reactive({
  id: undefined,
  shopName: '',
  address: '',
  notice: '',
  businessHours: '',
  takeoutEnabled: 1,
  cookNowEnabled: 1,
  wxpayEnabled: 1
})

const takeoutSwitch = computed({
  get: () => form.takeoutEnabled === 1,
  set: (value) => {
    form.takeoutEnabled = value ? 1 : 0
  }
})

const cookNowSwitch = computed({
  get: () => form.cookNowEnabled === 1,
  set: (value) => {
    form.cookNowEnabled = value ? 1 : 0
  }
})

const wxpaySwitch = computed({
  get: () => form.wxpayEnabled === 1,
  set: (value) => {
    form.wxpayEnabled = value ? 1 : 0
  }
})

const loadSetting = async () => {
  try {
    const data = await fetchCurrentShopSetting()
    if (!data) return
    Object.assign(form, data)
  } catch (error) {
    ElMessage.error(`店铺设置加载失败：${error.message}`)
  }
}

const handleSave = async () => {
  saving.value = true
  try {
    await saveShopSetting({ ...form })
    ElMessage.success('店铺设置保存成功')
    await loadSetting()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadSetting()
})
</script>

<style scoped>
.setting-grid {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 18px;
}

.section-card {
  padding: 22px;
}

.section-title {
  margin-bottom: 18px;
  font-size: 18px;
  font-weight: 700;
  color: #453015;
}

.switch-row {
  padding: 18px 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid rgba(130, 96, 42, 0.08);
  color: #71583a;
}
</style>
