import { request } from '../utils/request'

export function fetchSystemNotices() {
  return request('/admin/sys/notices')
}

export function createSystemNotice(payload) {
  return request('/admin/sys/notices', {
    method: 'POST',
    body: payload
  })
}

export function updateSystemNotice(id, payload) {
  return request(`/admin/sys/notices/${id}`, {
    method: 'PUT',
    body: payload
  })
}

export function deleteSystemNotice(id) {
  return request(`/admin/sys/notices/${id}`, {
    method: 'DELETE'
  })
}
