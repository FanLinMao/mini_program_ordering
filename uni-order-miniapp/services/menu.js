const DEFAULT_BASE_URL = 'http://localhost:18080'

function getBaseUrl() {
  return uni.getStorageSync('miniappBaseUrl') || DEFAULT_BASE_URL
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
          reject(new Error(`HTTP ${statusCode}`))
          return
        }
        if (!data || data.code !== 200) {
          reject(new Error(data?.message || 'Request failed'))
          return
        }
        resolve(data.data)
      },
      fail: (error) => {
        reject(new Error(error?.errMsg || 'Network Error'))
      }
    })
  })
}

export function fetchMenuCategories() {
  return request('/miniapp/menu/categories')
}
