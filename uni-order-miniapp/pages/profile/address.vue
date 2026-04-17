<template>
  <view class="page">
    <view class="toolbar">
      <text class="toolbar-title">收货地址</text>
      <button class="add-button" @tap="openEditor()">新增地址</button>
    </view>

    <view v-if="addresses.length" class="address-list">
      <view v-for="item in addresses" :key="item.id" class="address-card">
        <view class="address-head">
          <view class="address-user">
            <text class="contact-name">{{ item.contactName }}</text>
            <text class="contact-phone">{{ item.phone }}</text>
          </view>
          <view v-if="item.isDefault" class="default-tag">默认</view>
        </view>

        <text class="address-detail">{{ item.fullAddress }}</text>
        <text class="address-tag">{{ item.tag || '家' }}</text>

        <view class="action-row">
          <text class="action-link" @tap="openEditor(item)">编辑</text>
          <text v-if="!item.isDefault" class="action-link" @tap="handleSetDefault(item)">设为默认</text>
          <text class="action-link danger" @tap="handleDelete(item)">删除</text>
        </view>
      </view>
    </view>

    <view v-else class="empty-card">
      <text class="empty-title">还没有收货地址</text>
      <text class="empty-desc">新增一个常用地址，后面点餐会更方便。</text>
    </view>

    <view v-if="editorVisible" class="editor-mask" @tap="closeEditor">
      <view class="editor-panel" @tap.stop>
        <view class="editor-title">{{ form.id ? '编辑地址' : '新增地址' }}</view>

        <input v-model="form.contactName" class="editor-input" placeholder="收货人姓名" />
        <input v-model="form.phone" class="editor-input" placeholder="联系电话" />
        <input v-model="form.province" class="editor-input" placeholder="省份" />
        <input v-model="form.city" class="editor-input" placeholder="城市" />
        <input v-model="form.district" class="editor-input" placeholder="区县" />
        <input v-model="form.detailAddress" class="editor-input" placeholder="详细地址" />
        <input v-model="form.tag" class="editor-input" placeholder="标签，例如：家 / 公司" />

        <view class="switch-row">
          <text class="switch-label">设为默认地址</text>
          <switch :checked="form.isDefault === 1" color="#ffbf2f" @change="handleDefaultChange" />
        </view>

        <view class="editor-actions">
          <button class="ghost-button" @tap="closeEditor">取消</button>
          <button class="primary-button" @tap="handleSave">保存</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import {
  deleteMiniappAddress,
  fetchMiniappAddresses,
  saveMiniappAddress,
  setDefaultMiniappAddress
} from '@/services/profile'

const addresses = ref([])
const editorVisible = ref(false)
const form = ref(createEmptyForm())

function createEmptyForm() {
  return {
    id: null,
    contactName: '',
    phone: '',
    province: '',
    city: '',
    district: '',
    detailAddress: '',
    tag: '家',
    isDefault: 0
  }
}

async function loadData() {
  try {
    addresses.value = await fetchMiniappAddresses()
  } catch (error) {
    uni.showToast({
      title: error?.message || '地址加载失败',
      icon: 'none'
    })
  }
}

function openEditor(item) {
  form.value = item
    ? {
        id: Number(item.id),
        contactName: item.contactName || '',
        phone: item.phone || '',
        province: item.province || '',
        city: item.city || '',
        district: item.district || '',
        detailAddress: item.detailAddress || '',
        tag: item.tag || '家',
        isDefault: item.isDefault ? 1 : 0
      }
    : createEmptyForm()
  editorVisible.value = true
}

function closeEditor() {
  editorVisible.value = false
  form.value = createEmptyForm()
}

function handleDefaultChange(event) {
  form.value.isDefault = event.detail.value ? 1 : 0
}

async function handleSave() {
  try {
    await saveMiniappAddress(form.value)
    uni.showToast({
      title: '保存成功',
      icon: 'success'
    })
    closeEditor()
    loadData()
  } catch (error) {
    uni.showToast({
      title: error?.message || '保存失败',
      icon: 'none'
    })
  }
}

async function handleSetDefault(item) {
  try {
    await setDefaultMiniappAddress(item.id)
    uni.showToast({
      title: '已设为默认',
      icon: 'success'
    })
    loadData()
  } catch (error) {
    uni.showToast({
      title: error?.message || '设置失败',
      icon: 'none'
    })
  }
}

function handleDelete(item) {
  uni.showModal({
    title: '删除地址',
    content: '确认删除这个收货地址吗？',
    success: async (result) => {
      if (!result.confirm) return
      try {
        await deleteMiniappAddress(item.id)
        uni.showToast({
          title: '删除成功',
          icon: 'success'
        })
        loadData()
      } catch (error) {
        uni.showToast({
          title: error?.message || '删除失败',
          icon: 'none'
        })
      }
    }
  })
}

onShow(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  padding: 24rpx;
  background: #fdfaf4;
  box-sizing: border-box;
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.toolbar-title {
  color: #2f2418;
  font-size: 34rpx;
  font-weight: 700;
}

.add-button,
.ghost-button,
.primary-button {
  width: auto;
  height: 72rpx;
  line-height: 72rpx;
  margin: 0;
  border-radius: 999rpx;
  padding: 0 28rpx;
  font-size: 26rpx;
}

.add-button,
.primary-button {
  background: linear-gradient(135deg, #ffde56 0%, #ffc818 100%);
  color: #5a3c00;
}

.add-button::after,
.ghost-button::after,
.primary-button::after {
  border: 0;
}

.ghost-button {
  background: #fff3e1;
  color: #8a5b00;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 18rpx;
  margin-top: 20rpx;
}

.address-card,
.empty-card,
.editor-panel {
  background: #ffffff;
  border-radius: 28rpx;
}

.address-card {
  padding: 24rpx;
}

.address-head,
.address-user,
.action-row,
.switch-row,
.editor-actions {
  display: flex;
  align-items: center;
}

.address-head {
  justify-content: space-between;
  gap: 16rpx;
}

.address-user {
  gap: 18rpx;
  flex-wrap: wrap;
}

.contact-name {
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.contact-phone {
  color: #7f715f;
  font-size: 24rpx;
}

.default-tag {
  padding: 8rpx 18rpx;
  border-radius: 999rpx;
  background: #fff1c8;
  color: #b87a10;
  font-size: 22rpx;
}

.address-detail,
.address-tag {
  display: block;
}

.address-detail {
  margin-top: 16rpx;
  color: #4a3b2b;
  font-size: 26rpx;
  line-height: 1.7;
}

.address-tag {
  margin-top: 12rpx;
  color: #9f907f;
  font-size: 22rpx;
}

.action-row {
  gap: 28rpx;
  margin-top: 20rpx;
}

.action-link {
  color: #c98511;
  font-size: 24rpx;
}

.action-link.danger {
  color: #ff5d21;
}

.empty-card {
  margin-top: 20rpx;
  padding: 70rpx 24rpx;
  text-align: center;
}

.empty-title {
  display: block;
  color: #2f2418;
  font-size: 30rpx;
  font-weight: 700;
}

.empty-desc {
  display: block;
  margin-top: 14rpx;
  color: #9f907f;
  font-size: 24rpx;
}

.editor-mask {
  position: fixed;
  inset: 0;
  background: rgba(47, 36, 24, 0.35);
  padding: 40rpx 24rpx;
  box-sizing: border-box;
  display: flex;
  align-items: flex-end;
}

.editor-panel {
  width: 100%;
  padding: 28rpx 24rpx 24rpx;
  box-sizing: border-box;
}

.editor-title {
  color: #2f2418;
  font-size: 32rpx;
  font-weight: 700;
}

.editor-input {
  width: 100%;
  height: 84rpx;
  margin-top: 18rpx;
  padding: 0 24rpx;
  border-radius: 22rpx;
  background: #f9f6ef;
  color: #2f2418;
  box-sizing: border-box;
}

.switch-row {
  justify-content: space-between;
  margin-top: 18rpx;
  padding: 12rpx 0;
}

.switch-label {
  color: #2f2418;
  font-size: 26rpx;
}

.editor-actions {
  justify-content: flex-end;
  gap: 18rpx;
  margin-top: 26rpx;
}
</style>
