export const MINIAPP_API_CONFIG = {
  BASE_URL: 'http://47.109.207.85:18080',
  STORAGE_KEY: 'miniappBaseUrl'
}

export function getMiniappBaseUrl() {
  return uni.getStorageSync(MINIAPP_API_CONFIG.STORAGE_KEY) || MINIAPP_API_CONFIG.BASE_URL
}

export function setMiniappBaseUrl(baseUrl) {
  if (!baseUrl) {
    uni.removeStorageSync(MINIAPP_API_CONFIG.STORAGE_KEY)
    return
  }
  uni.setStorageSync(MINIAPP_API_CONFIG.STORAGE_KEY, String(baseUrl).trim())
}

export function clearMiniappBaseUrl() {
  uni.removeStorageSync(MINIAPP_API_CONFIG.STORAGE_KEY)
}
