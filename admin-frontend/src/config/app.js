function parseBoolean(value, defaultValue = false) {
  if (typeof value !== 'string') {
    return defaultValue
  }

  const normalized = value.trim().toLowerCase()
  if (['true', '1', 'yes', 'on'].includes(normalized)) {
    return true
  }
  if (['false', '0', 'no', 'off'].includes(normalized)) {
    return false
  }
  return defaultValue
}

export const APP_CONFIG = {
  API_BASE_URL: import.meta.env.VITE_API_BASE_URL,
  SSO_LOGIN: parseBoolean(import.meta.env.VITE_SSO_LOGIN, false)
}

export function getApiBaseUrl() {
  return (APP_CONFIG.API_BASE_URL || '').replace(/\/+$/, '')
}

export function isSsoLoginEnabled() {
  return Boolean(APP_CONFIG.SSO_LOGIN)
}
