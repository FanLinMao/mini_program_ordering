const DEFAULT_BASE_URL = 'http://localhost:18080'
const STORAGE_KEY = 'miniappUser'

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
          reject(new Error(data?.message || `HTTP ${statusCode}`))
          return
        }
        if (!data || data.code !== 200) {
          reject(new Error(data?.message || '请求失败'))
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

function uniLogin() {
  return new Promise((resolve, reject) => {
    uni.login({
      provider: 'weixin',
      success: resolve,
      fail: reject
    })
  })
}

function getWechatProfile() {
  return new Promise((resolve, reject) => {
    uni.getUserProfile({
      desc: '用于完善会员资料与订单服务',
      success: resolve,
      fail: reject
    })
  })
}

export function getMiniappUser() {
  return uni.getStorageSync(STORAGE_KEY) || null
}

export function isMiniappLoggedIn() {
  const user = getMiniappUser()
  return !!(user && user.userId && user.openid)
}

export async function loginMiniappUser() {
  const currentUser = getMiniappUser()
  const [loginResult, profileResult] = await Promise.all([
    uniLogin(),
    getWechatProfile()
  ])

  const userInfo = profileResult?.userInfo || {}
  const result = await request('/miniapp/auth/login', {
    method: 'POST',
    data: {
      code: loginResult?.code || '',
      openid: currentUser?.openid || '',
      nickname: userInfo.nickName || '',
      avatar: userInfo.avatarUrl || ''
    }
  })

  uni.setStorageSync(STORAGE_KEY, result)
  return result
}

export function logoutMiniappUser() {
  uni.removeStorageSync(STORAGE_KEY)
}
