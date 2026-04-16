import { request } from '../utils/request'

export function fetchMenuTree() {
  return request('/admin/sys/menus')
}

export function fetchSidebarMenus() {
  return request('/admin/sys/menus/sidebar')
}

export function createSystemMenu(payload) {
  return request('/admin/sys/menus', {
    method: 'POST',
    body: payload
  })
}

export function updateSystemMenu(id, payload) {
  return request(`/admin/sys/menus/${id}`, {
    method: 'PUT',
    body: payload
  })
}

export function deleteSystemMenu(id) {
  return request(`/admin/sys/menus/${id}`, {
    method: 'DELETE'
  })
}
