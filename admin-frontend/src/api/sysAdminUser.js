import { request } from '../utils/request'

export function fetchSysUserList() {
  return request('/admin/sys/users')
}

export function createSysUser(payload) {
  return request('/admin/sys/users', {
    method: 'POST',
    body: payload
  })
}

export function updateSysUser(id, payload) {
  return request(`/admin/sys/users/${id}`, {
    method: 'PUT',
    body: payload
  })
}

export function deleteSysUser(id) {
  return request(`/admin/sys/users/${id}`, {
    method: 'DELETE'
  })
}
