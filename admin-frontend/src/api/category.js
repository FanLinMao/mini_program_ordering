import { request } from '../utils/request'

export function fetchCategoryList() {
  return request('/admin/categories')
}

export function createCategory(data) {
  return request('/admin/categories', {
    method: 'POST',
    body: data
  })
}

export function updateCategory(id, data) {
  return request(`/admin/categories/${id}`, {
    method: 'PUT',
    body: data
  })
}

export function deleteCategory(id) {
  return request(`/admin/categories/${id}`, {
    method: 'DELETE'
  })
}
