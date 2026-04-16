import { request } from '../utils/request'

export function fetchPersonalization() {
  return request('/admin/sys/personalization')
}

export function savePersonalization(payload) {
  return request('/admin/sys/personalization', {
    method: 'PUT',
    body: payload
  })
}
