import { createRouter, createWebHistory } from 'vue-router'
import { fetchSidebarMenus } from '../api/systemMenu'
import { isLoggedIn } from '../utils/auth'
import { resolveViewComponent } from './viewRegistry'

const fallbackMenus = [
  { name: 'dashboard', title: '首页', path: '/dashboard', component: 'DashboardView' },
  { name: 'categories', title: '分类管理', path: '/categories', component: 'CategoryView' },
  { name: 'dishes', title: '菜品管理', path: '/dishes', component: 'DishView' },
  { name: 'orders', title: '订单管理', path: '/orders', component: 'OrderView' },
  { name: 'users', title: '用户管理', path: '/users', component: 'UserView' },
  { name: 'settings', title: '店铺设置', path: '/settings', component: 'SettingView' },
  {
    name: 'system-group',
    title: '系统设置',
    children: [
      {
        name: 'system-home-settings',
        title: '首页设置',
        path: '/system/home-settings',
        component: 'SystemHomeView'
      },
      {
        name: 'system-notices',
        title: '公告管理',
        path: '/system/notices',
        component: 'SystemNoticeView'
      },
      {
        name: 'system-menus',
        title: '菜单设置',
        path: '/system/menus',
        component: 'SystemMenuView'
      },
      {
        name: 'system-personalization',
        title: '个性化',
        path: '/system/personalization',
        component: 'SystemPersonalizationView'
      },
      {
        name: 'system-admin-users',
        title: '后台用户',
        path: '/system/admin-users',
        component: 'SystemAdminUserView'
      }
    ]
  }
]

const LoginView = resolveViewComponent('LoginView')
const AdminLayout = resolveViewComponent('AdminLayout')
const RouteNotFoundView = resolveViewComponent('RouteNotFoundView')

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/',
    name: 'admin-root',
    component: AdminLayout,
    redirect: '/dashboard',
    children: [
      {
        path: ':pathMatch(.*)*',
        name: 'admin-not-found',
        component: RouteNotFoundView,
        meta: {
          title: '404 页面',
          missingComponent: 'RouteNotFoundView'
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

let routesInitialized = false
let initPromise = null

function flattenMenuRoutes(menuTree, result = []) {
  ;(menuTree || []).forEach((item) => {
    if (item.children?.length) {
      flattenMenuRoutes(item.children, result)
      return
    }

    if (!item.path || !item.component) {
      return
    }

    result.push({
      name: item.name || item.path.replace(/[/:]/g, '-'),
      title: item.title || item.name || '页面',
      path: item.path,
      component: item.component
    })
  })
  return result
}

function registerDynamicRoutes(menuTree) {
  const routeItems = flattenMenuRoutes(menuTree)

  routeItems.forEach((item) => {
    if (router.hasRoute(item.name)) {
      return
    }

    const resolvedComponent = resolveViewComponent(item.component)
    const component = resolvedComponent || RouteNotFoundView
    const missingComponent = resolvedComponent ? '' : item.component

    router.addRoute('admin-root', {
      path: item.path.replace(/^\//, ''),
      name: item.name,
      component,
      meta: {
        title: item.title,
        component: item.component,
        missingComponent
      }
    })
  })
}

async function ensureDynamicRoutes() {
  if (routesInitialized) {
    return
  }

  if (initPromise) {
    return initPromise
  }

  initPromise = (async () => {
    try {
      const menuTree = await fetchSidebarMenus()
      registerDynamicRoutes(menuTree?.length ? menuTree : fallbackMenus)
    } catch (error) {
      registerDynamicRoutes(fallbackMenus)
    } finally {
      routesInitialized = true
    }
  })()

  return initPromise
}

router.beforeEach(async (to) => {
  if (to.path === '/login') {
    if (isLoggedIn()) {
      return '/dashboard'
    }
    return true
  }

  if (!isLoggedIn()) {
    return '/login'
  }

  await ensureDynamicRoutes()
  const resolved = router.resolve(to.fullPath)

  if (resolved.name && resolved.name !== to.name) {
    return {
      path: to.fullPath,
      replace: true
    }
  }

  return true
})

export default router
