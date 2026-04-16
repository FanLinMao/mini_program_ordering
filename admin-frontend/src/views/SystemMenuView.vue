<template>
  <div class="page-shell">
    <div class="page-header">
      <div>
        <h1 class="page-title">菜单设置</h1>
        <p class="page-desc">树形维护左侧导航菜单，保存后会同步刷新后台侧边栏。</p>
      </div>
      <el-button type="primary" @click="openCreateDialog()">新增菜单</el-button>
    </div>

    <section class="panel-card section-card">
      <el-table
        :data="rows"
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children' }"
        width="100%"
      >
        <el-table-column prop="title" label="菜单名称" min-width="180" />
        <el-table-column prop="name" label="路由名称" min-width="160" />
        <el-table-column prop="path" label="路由路径" min-width="220" />
        <el-table-column prop="component" label="组件路径" min-width="180" />
        <el-table-column label="菜单类型" width="110">
          <template #default="{ row }">
            <el-tag :type="row.type === 'group' ? 'warning' : 'primary'">
              {{ row.type === 'group' ? '分组' : '菜单' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="图标" min-width="180">
          <template #default="{ row }">
            <div class="icon-cell">
              <el-icon class="menu-icon-preview">
                <component :is="resolveIcon(row.icon)" />
              </el-icon>
              <span>{{ row.icon || '-' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="90" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="openCreateDialog(row.id)">新增下级</el-button>
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'create' ? '新增菜单' : '编辑菜单'"
      width="760px"
    >
      <el-form label-width="96px">
        <el-form-item label="上级菜单">
          <el-select v-model="form.parentId" placeholder="请选择上级菜单" style="width: 100%">
            <el-option :value="0" label="顶级菜单" />
            <el-option
              v-for="item in parentOptions"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="菜单名称">
          <el-input v-model="form.title" placeholder="例如：系统设置" />
        </el-form-item>

        <el-form-item label="菜单类型">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="分组" value="group" />
            <el-option label="菜单" value="menu" />
          </el-select>
        </el-form-item>

        <el-form-item label="路由名称">
          <el-input v-model="form.name" placeholder="例如：system-menus" />
        </el-form-item>

        <el-form-item v-if="form.type === 'menu'" label="路由路径">
          <el-input v-model="form.path" placeholder="例如：/system/menus" />
        </el-form-item>

        <el-form-item v-if="form.type === 'menu'" label="组件路径">
          <div class="component-form-row">
            <el-input v-model="form.component" placeholder="例如：SystemMenuView" />
            <el-select
              v-model="selectedComponent"
              placeholder="选择组件"
              style="width: 220px"
              @change="handleComponentSelect"
            >
              <el-option
                v-for="item in componentOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
        </el-form-item>

        <el-form-item label="图标">
          <div class="icon-form-row">
            <el-input v-model="form.icon" placeholder="例如：Setting / Grid / DataAnalysis" />
            <el-button class="icon-picker-trigger" @click="iconDialogVisible = true">
              选择图标
            </el-button>
            <div class="icon-form-preview">
              <el-icon>
                <component :is="resolveIcon(form.icon)" />
              </el-icon>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>

        <el-form-item label="状态">
          <el-switch v-model="form.statusSwitch" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="iconDialogVisible"
      title="选择图标"
      width="960px"
      top="6vh"
      class="icon-picker-dialog"
    >
      <div class="icon-picker-toolbar">
        <el-input
          v-model="iconKeyword"
          placeholder="搜索图标名称"
          clearable
        />
        <div class="icon-selected-name">
          当前值：<span>{{ form.icon || '未选择' }}</span>
        </div>
      </div>

      <el-tabs v-model="activeIconTab" class="icon-tabs">
        <el-tab-pane
          v-for="tab in iconTabs"
          :key="tab.name"
          :label="tab.label"
          :name="tab.name"
        >
          <div class="icon-grid">
            <button
              v-for="item in filteredIconsByTab(tab.name)"
              :key="item.name"
              type="button"
              class="icon-card"
              :class="{ active: form.icon === item.name }"
              @click="selectIcon(item.name)"
            >
              <el-icon class="icon-card-preview">
                <component :is="item.component" />
              </el-icon>
              <span class="icon-card-name">{{ item.name }}</span>
            </button>
          </div>
          <el-empty
            v-if="filteredIconsByTab(tab.name).length === 0"
            description="当前分类下没有匹配图标"
          />
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="iconDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {
  createSystemMenu,
  deleteSystemMenu,
  fetchMenuTree,
  updateSystemMenu
} from '../api/systemMenu'
import { availableViewOptions } from '../router/viewRegistry'

const rows = ref([])
const dialogVisible = ref(false)
const dialogMode = ref('create')
const selectedComponent = ref('')
const iconDialogVisible = ref(false)
const iconKeyword = ref('')
const activeIconTab = ref('common')
const componentOptions = availableViewOptions

const form = reactive({
  id: '',
  parentId: 0,
  title: '',
  name: '',
  path: '',
  component: '',
  icon: 'Setting',
  type: 'menu',
  sort: 0,
  statusSwitch: true
})

const iconRegistry = ElementPlusIconsVue

const allIcons = Object.entries(ElementPlusIconsVue)
  .map(([name, component]) => ({ name, component }))
  .sort((a, b) => a.name.localeCompare(b.name))

const commonIconNames = new Set([
  'House',
  'Grid',
  'List',
  'Tickets',
  'User',
  'Setting',
  'DataAnalysis',
  'Notification',
  'Menu',
  'FolderOpened',
  'Bell',
  'Search',
  'Document',
  'Files',
  'Edit',
  'Delete',
  'Plus',
  'Minus',
  'Refresh',
  'RefreshRight'
])

const iconTabs = [
  { name: 'common', label: '常用图标' },
  { name: 'direction', label: '方向图标' },
  { name: 'edit', label: '编辑操作' },
  { name: 'document', label: '文件文档' },
  { name: 'data', label: '数据分析' },
  { name: 'user', label: '用户业务' },
  { name: 'media', label: '媒体设备' },
  { name: 'all', label: '全部图标' }
]

const parentOptions = computed(() => {
  const result = []
  const walk = (list, prefix = '') => {
    list.forEach((item) => {
      result.push({
        id: item.id,
        label: `${prefix}${item.title}`
      })
      if (item.children?.length) {
        walk(item.children, `${prefix}${item.title} / `)
      }
    })
  }
  walk(rows.value)
  return result.filter((item) => item.id !== form.id)
})

function resolveIcon(iconName) {
  return iconRegistry[iconName] || ElementPlusIconsVue.Setting
}

function normalizeIconKeyword(value) {
  return (value || '').trim().toLowerCase()
}

function matchCategory(name, category) {
  const lowerName = name.toLowerCase()

  if (category === 'common') {
    return commonIconNames.has(name)
  }

  if (category === 'direction') {
    return ['arrow', 'caret', 'top', 'right', 'bottom', 'back', 'd-arrow', 'sort', 'rank']
      .some((keyword) => lowerName.includes(keyword))
  }

  if (category === 'edit') {
    return ['edit', 'delete', 'plus', 'minus', 'check', 'close', 'refresh', 'upload', 'download', 'copy']
      .some((keyword) => lowerName.includes(keyword))
  }

  if (category === 'document') {
    return ['document', 'folder', 'files', 'tickets', 'notebook', 'memo', 'reading', 'collection']
      .some((keyword) => lowerName.includes(keyword))
  }

  if (category === 'data') {
    return ['data', 'pie', 'trend', 'histogram', 'chart', 'monitor', 'cpu', 'coin']
      .some((keyword) => lowerName.includes(keyword))
  }

  if (category === 'user') {
    return ['user', 'avatar', 'male', 'female', 'service', 'office', 'postcard']
      .some((keyword) => lowerName.includes(keyword))
  }

  if (category === 'media') {
    return ['camera', 'video', 'mic', 'phone', 'headset', 'picture', 'film', 'printer']
      .some((keyword) => lowerName.includes(keyword))
  }

  return true
}

function filteredIconsByTab(tabName) {
  const keyword = normalizeIconKeyword(iconKeyword.value)

  return allIcons.filter((item) => {
    const matchesKeyword = !keyword || item.name.toLowerCase().includes(keyword)
    if (!matchesKeyword) {
      return false
    }
    if (tabName === 'all') {
      return true
    }
    return matchCategory(item.name, tabName)
  })
}

function resetForm() {
  form.id = ''
  form.parentId = 0
  form.title = ''
  form.name = ''
  form.path = ''
  form.component = ''
  form.icon = 'Setting'
  form.type = 'menu'
  form.sort = 0
  form.statusSwitch = true
  selectedComponent.value = ''
}

watch(
  () => form.type,
  (value) => {
    if (value === 'group') {
      form.path = ''
      form.component = ''
      selectedComponent.value = ''
    }
  }
)

async function loadRows() {
  try {
    rows.value = await fetchMenuTree()
  } catch (error) {
    ElMessage.error(`菜单加载失败：${error.message}`)
  }
}

function openCreateDialog(parentId = 0) {
  dialogMode.value = 'create'
  resetForm()
  form.parentId = parentId
  form.type = parentId === 0 ? 'group' : 'menu'
  dialogVisible.value = true
}

function openEditDialog(row) {
  dialogMode.value = 'edit'
  form.id = row.id
  form.parentId = row.parentId ?? 0
  form.title = row.title
  form.name = row.name
  form.path = row.path
  form.component = row.component
  form.icon = row.icon || 'Setting'
  form.type = row.type || 'menu'
  form.sort = row.sort
  form.statusSwitch = row.status === 1
  selectedComponent.value = row.component || ''
  dialogVisible.value = true
}

function handleComponentSelect(value) {
  form.component = value
}

function selectIcon(iconName) {
  form.icon = iconName
  iconDialogVisible.value = false
}

function notifySidebarRefresh() {
  window.dispatchEvent(new Event('admin-menu-changed'))
}

async function submitForm() {
  const payload = {
    parentId: form.parentId,
    title: form.title,
    name: form.name,
    path: form.path,
    component: form.component,
    icon: form.icon,
    type: form.type,
    sort: form.sort,
    status: form.statusSwitch ? 1 : 0
  }

  try {
    if (dialogMode.value === 'create') {
      await createSystemMenu(payload)
      ElMessage.success('菜单新增成功')
    } else {
      await updateSystemMenu(form.id, payload)
      ElMessage.success('菜单更新成功')
    }
    dialogVisible.value = false
    await loadRows()
    notifySidebarRefresh()
  } catch (error) {
    ElMessage.error(`保存失败：${error.message}`)
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除菜单“${row.title}”吗？`, '提示', { type: 'warning' })
    await deleteSystemMenu(row.id)
    ElMessage.success('删除成功')
    await loadRows()
    notifySidebarRefresh()
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

.icon-cell {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.menu-icon-preview {
  font-size: 18px;
  color: #4a617d;
}

.component-form-row {
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 220px;
  gap: 10px;
}

.icon-form-row {
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 132px 46px;
  gap: 10px;
  align-items: center;
}

.icon-picker-trigger {
  width: 132px;
}

.icon-form-preview {
  width: 46px;
  height: 40px;
  border-radius: 10px;
  border: 1px solid #dbe5f1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #425b78;
  font-size: 18px;
  background: #f8fbff;
}

.icon-picker-toolbar {
  margin-bottom: 14px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.icon-picker-toolbar .el-input {
  width: 320px;
}

.icon-selected-name {
  font-size: 13px;
  color: #6d7a8d;
}

.icon-selected-name span {
  color: #243142;
  font-weight: 600;
}

.icon-tabs :deep(.el-tabs__content) {
  max-height: 62vh;
  overflow: auto;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 12px;
}

.icon-card {
  min-height: 92px;
  padding: 14px 10px;
  border: 1px solid #dfe7f2;
  border-radius: 14px;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.18s ease;
}

.icon-card:hover {
  border-color: #7aa8ff;
  background: #f7fbff;
}

.icon-card.active {
  border-color: #4f86f4;
  background: #eef5ff;
}

.icon-card-preview {
  font-size: 24px;
  color: #324a69;
}

.icon-card-name {
  font-size: 12px;
  color: #55657d;
  text-align: center;
  word-break: break-all;
}
</style>
