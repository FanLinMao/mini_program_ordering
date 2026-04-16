import { request } from '../utils/request'

export function getCurrentThemeSetting(userCode = 'admin') {
  return request(`/admin/sys/theme-settings/current?userCode=${encodeURIComponent(userCode)}`)
}

export function saveThemeSetting(payload) {
  return request('/admin/sys/theme-settings', {
    method: 'POST',
    body: payload
  })
}
