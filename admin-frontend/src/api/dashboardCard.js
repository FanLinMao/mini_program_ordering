import { request } from '../utils/request'

export function fetchDashboardCards() {
  return request('/admin/sys/dashboard-cards')
}

export function createDashboardCard(payload) {
  return request('/admin/sys/dashboard-cards', {
    method: 'POST',
    body: payload
  })
}

export function updateDashboardCard(id, payload) {
  return request(`/admin/sys/dashboard-cards/${id}`, {
    method: 'PUT',
    body: payload
  })
}

export function deleteDashboardCard(id) {
  return request(`/admin/sys/dashboard-cards/${id}`, {
    method: 'DELETE'
  })
}
