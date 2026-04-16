import { request } from '../utils/request'

export function login(payload) {
  return request('/admin/sys/auth/login', {
    method: 'POST',
    body: payload
  })
}

export function logout(payload) {
  return request('/admin/sys/auth/logout', {
    method: 'POST',
    body: payload
  })
}
