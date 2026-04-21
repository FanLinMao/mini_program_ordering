import { request } from '../utils/request'

export function fetchDishList() {
  return request('/admin/dishes')
}

export function createDish(data) {
  return request('/admin/dishes', {
    method: 'POST',
    body: data
  })
}

export function updateDish(id, data) {
  return request(`/admin/dishes/${id}`, {
    method: 'PUT',
    body: data
  })
}

export function deleteDish(id) {
  return request(`/admin/dishes/${id}`, {
    method: 'DELETE'
  })
}

export function uploadDishImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request('/admin/files/dish-image', {
    method: 'POST',
    body: formData
  })
}
