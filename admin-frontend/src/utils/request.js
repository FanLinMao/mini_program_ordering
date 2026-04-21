import { getLoginUser } from './auth'
import { getApiBaseUrl } from '../config/app'

const BASE_URL = getApiBaseUrl()

export async function request(url, options = {}) {
  const loginUser = getLoginUser()
  const isFormData = typeof FormData !== 'undefined' && options.body instanceof FormData

  const response = await fetch(`${BASE_URL}${url}`, {
    method: options.method || 'GET',
    headers: {
      ...(isFormData ? {} : { 'Content-Type': 'application/json' }),
      ...(loginUser?.username ? { 'X-Login-User': loginUser.username } : {}),
      ...(options.headers || {})
    },
    body: options.body ? (isFormData ? options.body : JSON.stringify(options.body)) : undefined
  })

  if (!response.ok) {
    if (response.status === 413) {
      throw new Error('上传文件过大，请压缩图片，或调大 Nginx 的 client_max_body_size 和后端 multipart 限制')
    }
    throw new Error(`HTTP ${response.status}`)
  }

  const result = await response.json()

  if (result.code !== 200) {
    throw new Error(result.message || 'Request failed')
  }

  return result.data
}
