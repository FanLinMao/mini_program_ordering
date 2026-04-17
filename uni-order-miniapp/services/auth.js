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

function unsupportedEnvError() {
  return new Error('请在微信开发者工具或微信真机环境中测试授权登录')
}

function uniLogin() {
  return new Promise((resolve, reject) => {
    // #ifdef MP-WEIXIN
    uni.login({
      provider: 'weixin',
      success: resolve,
      fail: reject
    })
    // #endif

    // #ifndef MP-WEIXIN
    reject(unsupportedEnvError())
    // #endif
  })
}

function getWechatProfile() {
  return new Promise((resolve, reject) => {
    // #ifdef MP-WEIXIN
    if (typeof uni.getUserProfile !== 'function') {
      reject(new Error('当前微信环境不支持用户信息授权接口'))
      return
    }

    uni.getUserProfile({
      desc: '用于完善会员资料与订单服务',
      success: resolve,
      fail: reject
    })
    // #endif

    // #ifndef MP-WEIXIN
    reject(unsupportedEnvError())
    // #endif
  })
}

export function getMiniappUser() {
  const user = uni.getStorageSync(STORAGE_KEY)
  return user || null
}

export function isMiniappLoggedIn() {
  const user = getMiniappUser()
  return !!(user && user.userId && user.openid)
}

export async function loginMiniappUser() {
  const currentUser = getMiniappUser()
  const profileResult = await getWechatProfile()
  const loginResult = await uniLogin()
  const userInfo = profileResult?.userInfo || {}
  console.log('currentUser:', currentUser)
  console.log('profileResult:', profileResult)
  console.log('loginResult:', loginResult)
  console.log('userInfo:', userInfo)

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

export async function logoutMiniappUser() {
  const currentUser = getMiniappUser()
  if (currentUser?.userId || currentUser?.openid) {
    await request('/miniapp/auth/logout', {
      method: 'POST',
      data: {
        userId: currentUser?.userId || null,
        openid: currentUser?.openid || ''
      }
    })
  }

  uni.removeStorageSync(STORAGE_KEY)
}
