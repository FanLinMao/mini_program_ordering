import { request } from '../utils/request'

export function fetchUserList() {
  return request('/admin/users')
}

export function createUser(data) {
  return request('/admin/users', {
    method: 'POST',
    body: data
  })
}

export function updateUser(id, data) {
  return request(`/admin/users/${id}`, {
    method: 'PUT',
    body: data
  })
}

export function deleteUser(id) {
  return request(`/admin/users/${id}`, {
    method: 'DELETE'
  })
}
