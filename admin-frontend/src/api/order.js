import { request } from '../utils/request'

export function fetchOrderList() {
  return request('/admin/orders')
}

export function createOrder(data) {
  return request('/admin/orders', {
    method: 'POST',
    body: data
  })
}

export function updateOrder(id, data) {
  return request(`/admin/orders/${id}`, {
    method: 'PUT',
    body: data
  })
}

export function deleteOrder(id) {
  return request(`/admin/orders/${id}`, {
    method: 'DELETE'
  })
}
