const AUTH_USER_KEY = 'kitchen-admin-user'

export function saveLoginUser(user) {
  localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user))
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
}

export function isLoggedIn() {
  const user = getLoginUser()
  return Boolean(user?.token)
}
