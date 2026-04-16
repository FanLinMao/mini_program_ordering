import { request } from '../utils/request'

export function fetchCurrentUser(username) {
  return request(`/admin/sys/users/current?username=${encodeURIComponent(username)}`)
}

export function updateCurrentUserProfile(username, data) {
  return request(`/admin/sys/users/current/profile?username=${encodeURIComponent(username)}`, {
    method: 'PUT',
    body: data
  })
}

export function changeCurrentUserPassword(username, data) {
  return request(`/admin/sys/users/current/password?username=${encodeURIComponent(username)}`, {
    method: 'PUT',
    body: data
  })
}

export function fetchCurrentUserLoginLogs(username) {
  return request(`/admin/sys/users/current/login-logs?username=${encodeURIComponent(username)}`)
}
