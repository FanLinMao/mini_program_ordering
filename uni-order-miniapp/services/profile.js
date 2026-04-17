import { getMiniappUser } from '@/services/auth'

const DEFAULT_BASE_URL = 'http://localhost:18080'

function getBaseUrl() {
  return uni.getStorageSync('miniappBaseUrl') || DEFAULT_BASE_URL
}

function resolveMessage(payload, fallback) {
  return payload?.message || fallback
}

function request(url, options = {}) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${getBaseUrl()}${url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...(options.header || {})
      },
      success: (response) => {
        const { statusCode, data } = response
        if (statusCode !== 200) {
          reject(new Error(resolveMessage(data, `HTTP ${statusCode}`)))
          return
        }
        if (!data || data.code !== 200) {
          reject(new Error(resolveMessage(data, '请求失败，请稍后再试')))
          return
        }
        resolve(data.data)
      },
      fail: (error) => {
        reject(new Error(error?.errMsg || '网络异常，请稍后再试'))
      }
    })
  })
}

function getUserIdentity() {
  const user = getMiniappUser()
  if (!user?.userId || !user?.openid) {
    throw new Error('请先登录后再操作')
  }
  return {
    userId: user.userId,
    openid: user.openid
  }
}

function toQueryString(params = {}) {
  return Object.entries(params)
    .filter(([, value]) => value !== undefined && value !== null && value !== '')
    .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
    .join('&')
}

export function fetchProfileHome() {
  const identity = getUserIdentity()
  return request(`/miniapp/profile/home?${toQueryString(identity)}`)
}

export function fetchMiniappCoupons() {
  const identity = getUserIdentity()
  return request(`/miniapp/profile/coupons?${toQueryString(identity)}`)
}

export function fetchMiniappAddresses() {
  const identity = getUserIdentity()
  return request(`/miniapp/profile/addresses?${toQueryString(identity)}`)
}

export function saveMiniappAddress(payload) {
  const identity = getUserIdentity()
  return request('/miniapp/profile/address', {
    method: 'POST',
    data: {
      ...identity,
      ...payload
    }
  })
}

export function setDefaultMiniappAddress(addressId) {
  const identity = getUserIdentity()
  return request(`/miniapp/profile/address/default/${addressId}?${toQueryString(identity)}`, {
    method: 'POST'
  })
}

export function deleteMiniappAddress(addressId) {
  const identity = getUserIdentity()
  return request(`/miniapp/profile/address/delete/${addressId}?${toQueryString(identity)}`, {
    method: 'POST'
  })
}
