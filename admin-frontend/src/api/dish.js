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
