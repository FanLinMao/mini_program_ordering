const AUTH_USER_KEY = 'kitchen-admin-user'
const AUTH_USER_CHANGED_EVENT = 'admin-user-changed'

function emitUserChanged(user) {
  if (typeof window !== 'undefined') {
    window.dispatchEvent(new CustomEvent(AUTH_USER_CHANGED_EVENT, { detail: user || null }))
  }
}

export function saveLoginUser(user) {
  localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user))
  emitUserChanged(user)
}

export function updateLoginUser(patch) {
  const current = getLoginUser() || {}
  const nextUser = {
    ...current,
    ...(patch || {})
  }
  saveLoginUser(nextUser)
  return nextUser
}

export function getLoginUser() {
  try {
    const raw = localStorage.getItem(AUTH_USER_KEY)
    return raw ? JSON.parse(raw) : null
  } catch (error) {
    return null
  }
}

export function clearLoginUser() {
  localStorage.removeItem(AUTH_USER_KEY)
  emitUserChanged(null)
}

export function isLoggedIn() {
  const user = getLoginUser()
  return Boolean(user?.token)
}

export function getAuthUserChangedEvent() {
  return AUTH_USER_CHANGED_EVENT
}
