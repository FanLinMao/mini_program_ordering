import { request } from '../utils/request'

export function fetchCurrentShopSetting() {
  return request('/admin/shop-settings/current')
}

export function saveShopSetting(data) {
  return request('/admin/shop-settings', {
    method: 'POST',
    body: data
  })
}
