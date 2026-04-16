<template>
  <div class="admin-layout">
    <aside class="layout-sidebar" :class="{ collapsed: themeSettings.sidebarCollapsed }" :style="sidebarStyle">
      <div class="sidebar-brand">
        <div class="brand-avatar">
          <img v-if="brandConfig.logoUrl" :src="brandConfig.logoUrl" alt="logo" />
          <span v-else>{{ brandInitial }}</span>
        </div>

        <div v-if="!themeSettings.sidebarCollapsed" class="brand-meta">
          <div class="brand-title">{{ brandConfig.systemName }}</div>
          <div class="brand-subtitle">{{ brandConfig.systemIntro }}</div>
        </div>
      </div>

      <div class="sidebar-scroll">
        <template v-for="item in menuItems" :key="itemKey(item)">
          <div v-if="item.children?.length" class="menu-group">
            <button
              class="menu-item"
              :class="{ active: isGroupActive(item), expanded: isGroupExpanded(item) }"
              type="button"
              @click="toggleGroup(item)"
            >
              <span class="menu-main">
                <el-icon class="menu-icon">
                  <component :is="resolveIcon(item.icon)" />
                </el-icon>
                <span v-if="!themeSettings.sidebarCollapsed" class="menu-text">{{ item.title }}</span>
              </span>
              <el-icon v-if="!themeSettings.sidebarCollapsed" class="menu-arrow">
                <component :is="isGroupExpanded(item) ? ArrowDown : ArrowRight" />
              </el-icon>
            </button>

            <div v-if="isGroupExpanded(item) && !themeSettings.sidebarCollapsed" class="submenu-list">
              <button
                v-for="child in item.children"
                :key="itemKey(child)"
                class="submenu-item"
                :class="{ active: isMenuActive(child) }"
                type="button"
                @click="goMenu(child)"
              >
                <el-icon class="menu-icon">
                  <component :is="resolveIcon(child.icon)" />
                </el-icon>
                <span class="menu-text">{{ child.title }}</span>
              </button>
            </div>
          </div>

          <button
            v-else
            class="menu-item"
            :class="{ active: isMenuActive(item) }"
            type="button"
            @click="goMenu(item)"
          >
            <span class="menu-main">
              <el-icon class="menu-icon">
                <component :is="resolveIcon(item.icon)" />
              </el-icon>
              <span v-if="!themeSettings.sidebarCollapsed" class="menu-text">{{ item.title }}</span>
            </span>
          </button>
        </template>
      </div>
    </aside>

    <section class="layout-main">
      <header class="layout-header" :style="topbarStyle">
        <div class="header-left">
          <button class="header-icon" type="button" title="切换菜单" @click="toggleSidebarCollapse">
            <el-icon><Menu /></el-icon>
          </button>
          <div class="header-welcome">{{ brandConfig.welcomeText }}</div>
        </div>

        <div class="header-right">
          <label class="header-search">
            <el-icon><Search /></el-icon>
            <input v-model="searchKeyword" type="text" placeholder="搜索菜单、订单、用户" />
          </label>

          <button class="header-icon ghost" type="button" title="消息中心">
            <el-icon><Bell /></el-icon>
          </button>

          <div class="header-user">
            <div class="header-user-avatar">{{ currentUserInitial }}</div>
            <span class="header-user-name">{{ currentUserName }}</span>
          </div>

          <button class="header-icon ghost" type="button" title="主题设置" @click="themeDrawerVisible = true">
            <el-icon><Setting /></el-icon>
          </button>
        </div>
      </header>

      <div class="layout-tabs">
        <div class="tab-list">
          <button
            v-for="tab in openTabs"
            :key="tab.path"
            class="tab-chip"
            :class="{ active: activeTabPath === tab.path }"
            type="button"
            @click="goTab(tab)"
          >
            <span>{{ tab.title }}</span>
            <el-icon v-if="tab.closable" class="tab-close" @click.stop="closeTab(tab)">
              <Close />
            </el-icon>
          </button>
        </div>

        <div class="tab-tools">
          <button class="tab-tool-btn" type="button" title="刷新当前页面" @click="refreshCurrentPage">
            <el-icon><RefreshRight /></el-icon>
          </button>
          <button class="tab-tool-btn" type="button" :title="fullscreenTitle" @click="toggleFullscreen">
            <el-icon>
              <component :is="isFullscreen ? Close : FullScreen" />
            </el-icon>
          </button>
        </div>
      </div>

      <div ref="contentRef" class="layout-content">
        <div class="content-scroll">
          <router-view v-slot="{ Component, route: viewRoute }">
            <component :is="Component" :key="`${viewRoute.fullPath}-${refreshKey}`" />
          </router-view>
        </div>
        <AdminFooter />
      </div>
    </section>

    <el-drawer
      v-model="themeDrawerVisible"
      title="主题设置"
      direction="rtl"
      size="360px"
      class="theme-drawer"
      append-to-body
    >
      <div class="drawer-body">
        <section class="drawer-section">
          <div class="drawer-title">导航栏布局</div>
          <div class="layout-options">
            <button
              v-for="item in layoutOptions"
              :key="item.value"
              type="button"
              class="layout-card"
              :class="{ active: themeSettings.navLayout === item.value }"
              @click="themeSettings.navLayout = item.value"
            >
              <div class="layout-card-title">{{ item.label }}</div>
              <div class="layout-card-desc">{{ item.desc }}</div>
            </button>
          </div>
        </section>

        <section class="drawer-section">
          <div class="drawer-title">顶部导航颜色</div>
          <div class="palette-list">
            <button
              v-for="item in topbarPalettes"
              :key="item.value"
              type="button"
              class="palette-card"
              :class="{ active: themeSettings.topbarColor === item.value }"
              @click="themeSettings.topbarColor = item.value"
            >
              <span
                class="palette-chip"
                :style="{ background: `linear-gradient(135deg, ${item.start}, ${item.end})` }"
              ></span>
              <span>{{ item.label }}</span>
            </button>
          </div>
        </section>

        <section class="drawer-section">
          <div class="drawer-title">侧边栏颜色</div>
          <div class="palette-list">
            <button
              v-for="item in sidebarPalettes"
              :key="item.value"
              type="button"
              class="palette-card"
              :class="{ active: themeSettings.sidebarColor === item.value }"
              @click="themeSettings.sidebarColor = item.value"
            >
              <span
                class="palette-chip"
                :style="{ background: `linear-gradient(135deg, ${item.start}, ${item.end})` }"
              ></span>
              <span>{{ item.label }}</span>
            </button>
          </div>
        </section>

        <section class="drawer-section">
          <div class="drawer-title">菜单状态</div>
          <div class="switch-row">
            <div>
              <div class="switch-title">默认折叠菜单</div>
              <div class="switch-desc">保存当前管理员的左侧菜单展开状态。</div>
            </div>
            <el-switch v-model="themeSettings.sidebarCollapsed" />
          </div>
        </section>

        <section class="drawer-section">
          <div class="drawer-title">效果预览</div>
          <div class="preview-card">
            <div
              class="preview-topbar"
              :style="{ background: `linear-gradient(90deg, ${currentTopbarPalette.start}, ${currentTopbarPalette.end})` }"
            ></div>
            <div class="preview-main">
              <div
                class="preview-sidebar"
                :style="{ background: `linear-gradient(180deg, ${currentSidebarPalette.start}, ${currentSidebarPalette.end})` }"
              ></div>
              <div class="preview-content">
                <div class="preview-line short"></div>
                <div class="preview-line"></div>
                <div class="preview-line"></div>
              </div>
            </div>
          </div>
        </section>
      </div>

      <template #footer>
        <div class="drawer-footer">
          <el-button @click="resetThemeSettings">恢复默认</el-button>
          <el-button type="primary" :loading="savingTheme" @click="saveThemeSettings">
            保存设置
          </el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ArrowDown,
  ArrowRight,
  Bell,
  Close,
  DataAnalysis,
  FolderOpened,
  FullScreen,
  Grid,
  House,
  List,
  Menu,
  Notification,
  RefreshRight,
  Search,
  Setting,
  Tickets,
  User
} from '@element-plus/icons-vue'
import AdminFooter from '../../components/AdminFooter.vue'
import { getCurrentThemeSetting, saveThemeSetting } from '../../api/themeSetting'
import { fetchSidebarMenus } from '../../api/systemMenu'
import { fetchPersonalization } from '../../api/personalization'
import { getLoginUser } from '../../utils/auth'
import { applyGlobalBackgroundImage, applyGlobalThemeColor, getDefaultThemeColor } from '../../utils/appTheme'

const CURRENT_USER_CODE = 'admin'

const DEFAULT_THEME = {
  navLayout: 'side',
  topbarColor: 'linear-gradient(90deg, #34c06a 0%, #46b876 100%)',
  sidebarColor: 'linear-gradient(180deg, #243244 0%, #26364b 100%)',
  sidebarCollapsed: false
}

const topbarPalettes = [
  {
    label: '生鲜绿',
    value: 'linear-gradient(90deg, #34c06a 0%, #46b876 100%)',
    start: '#34c06a',
    end: '#46b876'
  },
  {
    label: '天空蓝',
    value: 'linear-gradient(90deg, #4d81f7 0%, #5c92ff 100%)',
    start: '#4d81f7',
    end: '#5c92ff'
  },
  {
    label: '活力橙',
    value: 'linear-gradient(90deg, #f59e0b 0%, #f97316 100%)',
    start: '#f59e0b',
    end: '#f97316'
  },
  {
    label: '夜幕黑',
    value: 'linear-gradient(90deg, #111827 0%, #334155 100%)',
    start: '#111827',
    end: '#334155'
  }
]

const sidebarPalettes = [
  {
    label: '经典深蓝',
    value: 'linear-gradient(180deg, #243244 0%, #26364b 100%)',
    start: '#243244',
    end: '#26364b'
  },
  {
    label: '森林绿',
    value: 'linear-gradient(180deg, #163b2c 0%, #22543d 100%)',
    start: '#163b2c',
    end: '#22543d'
  },
  {
    label: '咖啡棕',
    value: 'linear-gradient(180deg, #3b2f1f 0%, #5a472e 100%)',
    start: '#3b2f1f',
    end: '#5a472e'
  },
  {
    label: '深夜蓝',
    value: 'linear-gradient(180deg, #1f2937 0%, #111827 100%)',
    start: '#1f2937',
    end: '#111827'
  }
]

const layoutOptions = [
  { label: '经典布局', value: 'classic', desc: '左侧菜单与顶部导航分区显示。' },
  { label: '紧凑布局', value: 'compact', desc: '更适合内容密度更高的后台页面。' }
]

const iconMap = {
  House,
  Grid,
  Tickets,
  User,
  Setting,
  Notification,
  FolderOpened,
  DataAnalysis,
  List
}

const fallbackMenus = [
  { name: 'dashboard', title: '首页', path: '/dashboard', icon: 'House' },
  { name: 'categories', title: '分类管理', path: '/categories', icon: 'Grid' },
  { name: 'dishes', title: '菜品管理', path: '/dishes', icon: 'Tickets' },
  { name: 'orders', title: '订单管理', path: '/orders', icon: 'List' },
  { name: 'users', title: '商户管理', path: '/users', icon: 'User' },
  { name: 'settings', title: '店铺设置', path: '/settings', icon: 'Setting' },
  {
    name: 'system-group',
    title: '系统设置',
    icon: 'FolderOpened',
    children: [
      { name: 'system-home-settings', title: '首页设置', path: '/system/home-settings', icon: 'DataAnalysis' },
      { name: 'system-notices', title: '公告管理', path: '/system/notices', icon: 'Notification' },
      { name: 'system-menus', title: '菜单设置', path: '/system/menus', icon: 'Grid' },
      { name: 'system-personalization', title: '个性化', path: '/system/personalization', icon: 'Setting' },
      { name: 'system-admin-users', title: '后台用户', path: '/system/admin-users', icon: 'User' }
    ]
  }
]

const router = useRouter()
const route = useRoute()
const loginUser = getLoginUser()

const themeDrawerVisible = ref(false)
const savingTheme = ref(false)
const searchKeyword = ref('')
const refreshKey = ref(0)
const isFullscreen = ref(false)
const menuItems = ref([])
const expandedGroups = ref([])
const openTabs = ref([{ title: '首页', path: '/dashboard', closable: false }])
const activeTabPath = ref('/dashboard')
const contentRef = ref(null)

const themeSettings = reactive({
  ...DEFAULT_THEME
})

const brandConfig = reactive({
  logoUrl: '',
  systemName: 'Kitchen Admin',
  systemIntro: '私人厨房后台管理',
  welcomeText: '欢迎使用私人厨房后台管理系统'
})

const currentUserName = computed(() => loginUser?.nickname || loginUser?.username || 'Admin')
const currentUserInitial = computed(() => currentUserName.value.slice(0, 1).toUpperCase())
const brandInitial = computed(() => (brandConfig.systemName || 'K').slice(0, 1).toUpperCase())
const fullscreenTitle = computed(() => (isFullscreen.value ? '退出全屏' : '系统全屏'))
const topbarStyle = computed(() => ({ background: themeSettings.topbarColor }))
const sidebarStyle = computed(() => ({ background: themeSettings.sidebarColor }))
const currentTopbarPalette = computed(() => {
  return topbarPalettes.find((item) => item.value === themeSettings.topbarColor) || topbarPalettes[0]
})
const currentSidebarPalette = computed(() => {
  return sidebarPalettes.find((item) => item.value === themeSettings.sidebarColor) || sidebarPalettes[0]
})

function itemKey(item) {
  return item.id || item.name || item.path || item.title
}

function resolveIcon(iconName) {
  return iconMap[iconName] || FolderOpened
}

function isMenuActive(item) {
  return item.path === route.path
}

function isGroupActive(item) {
  return item.children?.some((child) => child.path === route.path)
}

function isGroupExpanded(item) {
  return expandedGroups.value.includes(itemKey(item))
}

function findParentsByPath(items, path, parents = []) {
  for (const item of items || []) {
    if (item.path === path) return parents
    if (item.children?.length) {
      const result = findParentsByPath(item.children, path, [...parents, itemKey(item)])
      if (result) return result
    }
  }
  return null
}

function ensureGroupExpandByRoute(path) {
  if (path === '/dashboard') {
    expandedGroups.value = []
    return
  }
  expandedGroups.value = [...new Set(findParentsByPath(menuItems.value, path) || [])]
}

function toggleGroup(item) {
  const key = itemKey(item)
  if (themeSettings.sidebarCollapsed) {
    themeSettings.sidebarCollapsed = false
    expandedGroups.value = [key]
    return
  }
  if (expandedGroups.value.includes(key)) {
    expandedGroups.value = expandedGroups.value.filter((it) => it !== key)
  } else {
    expandedGroups.value = [...expandedGroups.value, key]
  }
}

function goMenu(item) {
  if (item.path) {
    router.push(item.path)
  }
}

function normalizeMenus(items = []) {
  return items.map((item) => ({
    ...item,
    title: item.title || item.name || '未命名菜单',
    children: item.children?.length ? normalizeMenus(item.children) : []
  }))
}

async function loadSidebarMenus() {
  try {
    const data = await fetchSidebarMenus()
    menuItems.value = normalizeMenus(data?.length ? data : fallbackMenus)
  } catch (error) {
    menuItems.value = normalizeMenus(fallbackMenus)
  } finally {
    ensureGroupExpandByRoute(route.path)
  }
}

function menuTitleByPath(path, items = menuItems.value) {
  for (const item of items) {
    if (item.path === path) return item.title
    if (item.children?.length) {
      const result = menuTitleByPath(path, item.children)
      if (result) return result
    }
  }
  return path === '/dashboard' ? '首页' : ''
}

function upsertTabByRoute(targetRoute) {
  const path = targetRoute.path
  const title = targetRoute.meta?.title || menuTitleByPath(path) || '页面'
  const exists = openTabs.value.find((item) => item.path === path)
  if (!exists) {
    openTabs.value.push({
      title,
      path,
      closable: path !== '/dashboard'
    })
  } else {
    exists.title = title
  }
  activeTabPath.value = path
}

function goTab(tab) {
  router.push(tab.path)
}

function closeTab(tab) {
  if (!tab.closable) return
  const index = openTabs.value.findIndex((item) => item.path === tab.path)
  if (index === -1) return
  const isCurrent = activeTabPath.value === tab.path
  openTabs.value.splice(index, 1)
  if (isCurrent) {
    const nextTab = openTabs.value[index - 1] || openTabs.value[index] || openTabs.value[0]
    if (nextTab) router.push(nextTab.path)
  }
}

function refreshCurrentPage() {
  refreshKey.value += 1
  ElMessage.success('当前页面已刷新')
}

async function toggleFullscreen() {
  const element = document.documentElement
  try {
    if (!document.fullscreenElement) {
      await element.requestFullscreen()
      isFullscreen.value = true
    } else {
      await document.exitFullscreen()
      isFullscreen.value = false
    }
  } catch (error) {
    ElMessage.error('全屏切换失败')
  }
}

function syncFullscreenState() {
  isFullscreen.value = Boolean(document.fullscreenElement)
}

function toggleSidebarCollapse() {
  themeSettings.sidebarCollapsed = !themeSettings.sidebarCollapsed
}

function applyThemeSettings(data = {}) {
  themeSettings.navLayout = data.navLayout || DEFAULT_THEME.navLayout
  themeSettings.topbarColor = data.topbarColor || DEFAULT_THEME.topbarColor
  themeSettings.sidebarColor = data.sidebarColor || DEFAULT_THEME.sidebarColor
  themeSettings.sidebarCollapsed = Boolean(data.sidebarCollapsed)
}

function applyBrandConfig(data = {}) {
  brandConfig.logoUrl = data.logoUrl || ''
  brandConfig.systemName = data.systemName || 'Kitchen Admin'
  brandConfig.systemIntro = data.systemIntro || '私人厨房后台管理'
  brandConfig.welcomeText = data.welcomeText || '欢迎使用私人厨房后台管理系统'
}

async function loadThemeSettings() {
  try {
    const data = await getCurrentThemeSetting(CURRENT_USER_CODE)
    applyThemeSettings(data || {})
  } catch (error) {
    applyThemeSettings(DEFAULT_THEME)
  }
}

async function loadPersonalizationTheme() {
  try {
    const data = await fetchPersonalization()
    applyBrandConfig(data || {})
    applyGlobalThemeColor(data?.themeColor || getDefaultThemeColor())
    applyGlobalBackgroundImage(data?.systemBackgroundImage || '')
  } catch (error) {
    applyBrandConfig({})
    applyGlobalThemeColor(getDefaultThemeColor())
    applyGlobalBackgroundImage('')
  }
}

function resetThemeSettings() {
  applyThemeSettings(DEFAULT_THEME)
}

async function saveThemeSettings() {
  savingTheme.value = true
  try {
    await saveThemeSetting({
      userCode: CURRENT_USER_CODE,
      ...themeSettings
    })
    ElMessage.success('主题设置已保存')
    themeDrawerVisible.value = false
  } catch (error) {
    ElMessage.error(error.message || '主题设置保存失败')
  } finally {
    savingTheme.value = false
  }
}

function handleMenuChanged() {
  loadSidebarMenus()
}

function handlePersonalizationChanged(event) {
  const data = event?.detail || {}
  applyBrandConfig(data)
  applyGlobalThemeColor(data.themeColor || getDefaultThemeColor())
  applyGlobalBackgroundImage(data.systemBackgroundImage || '')
}

watch(
  () => route.fullPath,
  () => {
    upsertTabByRoute(route)
    ensureGroupExpandByRoute(route.path)
  },
  { immediate: true }
)

onMounted(async () => {
  document.addEventListener('fullscreenchange', syncFullscreenState)
  window.addEventListener('admin-menu-changed', handleMenuChanged)
  window.addEventListener('admin-personalization-changed', handlePersonalizationChanged)
  await Promise.all([loadThemeSettings(), loadSidebarMenus(), loadPersonalizationTheme()])
})

onBeforeUnmount(() => {
  document.removeEventListener('fullscreenchange', syncFullscreenState)
  window.removeEventListener('admin-menu-changed', handleMenuChanged)
  window.removeEventListener('admin-personalization-changed', handlePersonalizationChanged)
})
</script>

<style scoped>
.admin-layout {
  width: 100%;
  height: 100vh;
  display: flex;
  overflow: hidden;
  background: var(--app-layout-background);
}

.layout-sidebar {
  width: 212px;
  flex: none;
  display: flex;
  flex-direction: column;
  color: #f5f7fb;
  border-right: 1px solid rgba(255, 255, 255, 0.08);
  transition: width 0.24s ease;
}

.layout-sidebar.collapsed {
  width: 72px;
}

.sidebar-brand {
  min-height: 64px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 16px;
}

.brand-avatar {
  width: 30px;
  height: 30px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.14);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  overflow: hidden;
  flex: none;
}

.brand-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.brand-meta {
  min-width: 0;
}

.brand-title {
  font-size: 15px;
  font-weight: 700;
  line-height: 1.2;
  color: #ffffff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.brand-subtitle {
  margin-top: 2px;
  font-size: 12px;
  color: rgba(229, 236, 244, 0.72);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sidebar-toggle {
  margin-left: auto;
  color: #ffffff;
}

.sidebar-scroll {
  flex: 1;
  overflow-y: auto;
  padding: 10px 8px 18px;
}

.sidebar-scroll::-webkit-scrollbar {
  width: 0;
  height: 0;
}

.menu-item,
.submenu-item {
  width: 100%;
  min-height: 42px;
  border: none;
  background: transparent;
  color: inherit;
  cursor: pointer;
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 14px;
  border-radius: 12px;
  margin-bottom: 8px;
  transition: background 0.2s ease, color 0.2s ease;
}

.menu-item:hover,
.submenu-item:hover {
  background: rgba(255, 255, 255, 0.08);
}

.menu-item.active,
.menu-item.expanded,
.submenu-item.active {
  background: rgba(255, 255, 255, 0.18);
  color: #ffffff;
}

.menu-main {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.menu-icon {
  font-size: 17px;
  flex: none;
}

.menu-text {
  font-size: 14px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.menu-arrow {
  font-size: 12px;
  opacity: 0.88;
}

.submenu-list {
  padding: 0 0 8px 18px;
}

.submenu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 12px;
  border-radius: 12px;
  margin-bottom: 6px;
}

.layout-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.layout-header {
  height: 58px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 18px;
  color: #ffffff;
}

.header-left,
.header-right {
  display: flex;
  align-items: center;
  gap: 14px;
}

.header-welcome {
  font-size: 15px;
  font-weight: 700;
  white-space: nowrap;
}

.header-search {
  width: 280px;
  height: 34px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 14px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.14);
}

.header-search input {
  width: 100%;
  border: none;
  outline: none;
  background: transparent;
  color: #ffffff;
  font-size: 14px;
}

.header-search input::placeholder {
  color: rgba(255, 255, 255, 0.76);
}

.header-icon {
  width: 34px;
  height: 34px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  background: transparent;
  color: #ffffff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.header-icon.ghost {
  background: rgba(255, 255, 255, 0.1);
}

.header-user {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: rgba(27, 44, 73, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
}

.header-user-name {
  font-size: 14px;
  font-weight: 600;
}

.layout-tabs {
  min-height: 46px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 0 16px;
  background: rgba(255, 255, 255, 0.96);
  border-bottom: 1px solid #e6edf5;
}

.tab-list {
  display: flex;
  align-items: center;
  gap: 8px;
  overflow-x: auto;
}

.tab-list::-webkit-scrollbar {
  width: 0;
  height: 0;
}

.tab-chip {
  height: 32px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 14px;
  border-radius: 10px;
  border: 1px solid #d8e4f0;
  background: #ffffff;
  color: #5b6a80;
  cursor: pointer;
  flex: none;
}

.tab-chip.active {
  color: var(--app-theme-color);
  background: color-mix(in srgb, var(--app-theme-color) 10%, #ffffff);
  border-color: color-mix(in srgb, var(--app-theme-color) 24%, #ffffff);
}

.tab-close {
  font-size: 12px;
}

.tab-tools {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tab-tool-btn {
  width: 30px;
  height: 30px;
  border-radius: 8px;
  border: 1px solid #e1e9f2;
  background: #ffffff;
  color: #8091a7;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.layout-content {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.content-scroll {
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: 18px;
}

.content-scroll::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.content-scroll::-webkit-scrollbar-thumb {
  border-radius: 999px;
  background: rgba(129, 145, 167, 0.28);
}

.drawer-body {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.drawer-section {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.drawer-title {
  color: #49586d;
  font-size: 15px;
  font-weight: 700;
}

.layout-options {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.layout-card {
  padding: 14px 12px;
  border-radius: 14px;
  border: 1px solid #d9e7f1;
  background: #ffffff;
  cursor: pointer;
  text-align: left;
  transition: all 0.2s ease;
}

.layout-card.active {
  border-color: #63c8df;
  background: linear-gradient(180deg, rgba(99, 200, 223, 0.14), rgba(99, 200, 223, 0.06));
  box-shadow: 0 0 0 3px rgba(99, 200, 223, 0.12);
}

.layout-card-title {
  color: #2f3b52;
  font-size: 14px;
  font-weight: 700;
}

.layout-card-desc {
  margin-top: 6px;
  color: #7f8ca0;
  font-size: 12px;
  line-height: 1.5;
}

.palette-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.palette-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 14px;
  border: 1px solid #d9e7f1;
  background: #ffffff;
  color: #5f6f85;
  cursor: pointer;
  transition: all 0.2s ease;
}

.palette-card.active {
  border-color: #63c8df;
  box-shadow: 0 0 0 3px rgba(99, 200, 223, 0.12);
  color: #2f3b52;
}

.palette-chip {
  width: 44px;
  height: 24px;
  border-radius: 999px;
  flex: none;
}

.switch-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.switch-title {
  color: #2f3b52;
  font-size: 14px;
  font-weight: 700;
}

.switch-desc {
  margin-top: 4px;
  color: #8b97aa;
  font-size: 12px;
}

.preview-card {
  border-radius: 18px;
  border: 1px solid #e2ebf3;
  background: #f7fafc;
  overflow: hidden;
}

.preview-topbar {
  height: 36px;
}

.preview-main {
  display: flex;
  min-height: 132px;
}

.preview-sidebar {
  width: 72px;
}

.preview-content {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.preview-line {
  height: 10px;
  border-radius: 999px;
  background: #dfe8f1;
}

.preview-line.short {
  width: 48%;
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.theme-drawer .el-drawer__header) {
  margin-bottom: 0;
  padding: 18px 18px 10px;
  color: #2f3b52;
  font-size: 16px;
  font-weight: 700;
}

:deep(.theme-drawer .el-drawer__body) {
  padding: 12px 18px 18px;
}

:deep(.theme-drawer .el-drawer__footer) {
  padding: 14px 18px 18px;
  border-top: none;
}

:deep(.el-drawer__header) {
  margin-bottom: 12px;
  padding: 18px 18px 0;
  color: #2f3b52;
  font-size: 16px;
  font-weight: 700;
}

:deep(.el-drawer__body) {
  padding: 10px 18px 24px;
}

:deep(.el-radio-group) {
  display: inline-flex;
}

:deep(.el-radio-button__inner) {
  min-width: 108px;
  height: 38px;
  border-radius: 8px;
  border-color: #d8e5ef;
  color: #5f6f85;
  font-weight: 600;
  box-shadow: none;
}

:deep(.el-radio-button:first-child .el-radio-button__inner) {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}

:deep(.el-radio-button:last-child .el-radio-button__inner) {
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
}

:deep(.el-radio-button.is-active .el-radio-button__inner) {
  background: #5bc0de;
  border-color: #5bc0de;
  color: #ffffff;
  box-shadow: none;
}

@media (max-width: 1280px) {
  .header-search {
    width: 220px;
  }

  .header-welcome {
    max-width: 320px;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>
