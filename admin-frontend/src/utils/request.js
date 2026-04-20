import { getLoginUser } from './auth'

const BASE_URL = 'http://localhost:18080'

export async function request(url, options = {}) {
  const loginUser = getLoginUser()

  const response = await fetch(`${BASE_URL}${url}`, {
    method: options.method || 'GET',
    headers: {
      'Content-Type': 'application/json',
      ...(loginUser?.username ? { 'X-Login-User': loginUser.username } : {}),
      ...(options.headers || {})
    },
    body: options.body ? JSON.stringify(options.body) : undefined
  })

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}`)
  }

  const result = await response.json()

  if (result.code !== 200) {
    throw new Error(result.message || 'Request failed')
  }

  return result.data
}
