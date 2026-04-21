import { addMockOrder } from '@/mock/orders'
import { getMiniappUser } from '@/services/auth'
import { getMiniappBaseUrl } from '@/config/api'

function resolveErrorMessage(error, fallback = '请求失败') {
  if (!error) return fallback
  if (typeof error === 'string' && error.trim()) return error
  if (typeof error?.message === 'string' && error.message.trim()) return error.message
  if (typeof error?.errMsg === 'string' && error.errMsg.trim()) return error.errMsg
  if (typeof error?.data?.message === 'string' && error.data.message.trim()) return error.data.message
  return fallback
}

function request(url, options = {}) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${getMiniappBaseUrl()}${url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...(options.header || {})
      },
      success: (response) => {
        const { statusCode, data } = response
        if (statusCode !== 200) {
          reject({
            message: resolveErrorMessage(data, `HTTP ${statusCode}`),
            statusCode,
            data
          })
          return
        }
        if (!data || data.code !== 200) {
          reject({
            message: resolveErrorMessage(data, '请求失败'),
            statusCode,
            data
          })
          return
        }
        resolve(data.data)
      },
      fail: (error) => {
        reject({
          message: resolveErrorMessage(error, '网络异常，请检查后端服务是否已启动'),
          error
        })
      }
    })
  })
}

function normalizeOrderForMock(order = {}) {
  return {
    id: order.orderNo || order.orderId || `${Date.now()}`,
    title: order.title || '新订单',
    time: order.time || '刚刚下单',
    amount: Number(order.amount || 0).toFixed(2),
    status: order.status || 'pendingPay',
    statusText: order.statusText || '待支付',
    note: order.note || '请在 15 分钟内完成支付',
    shopName: order.shopName || '私人厨房',
    pickupType: order.pickupType || '到店自取',
    contactName: order.contactName || '微信用户',
    items: (order.items || []).map((item, index) => ({
      id: item.id || `ITEM${index + 1}`,
      name: item.name,
      count: item.count,
      price: Number(item.price || 0)
    }))
  }
}

export async function submitCookNowOrder(orderPayload) {
  const currentUser = getMiniappUser()

  if (!currentUser || !currentUser.userId || !currentUser.openid) {
    throw {
      message: '请先登录后再下单'
    }
  }

  try {
    const result = await request('/miniapp/orders/cook-now', {
      method: 'POST',
      data: {
        userId: currentUser.userId,
        openid: currentUser.openid,
        payType: 'cookNow',
        items: orderPayload.items.map((item) => ({
          id: Number(item.id),
          count: item.count
        }))
      }
    })

    const normalizedOrder = normalizeOrderForMock(result)
    addMockOrder(normalizedOrder)

    return {
      orderNo: result.orderNo,
      created: true,
      payload: orderPayload,
      order: normalizedOrder,
      message: '下单成功，已提交至后端'
    }
  } catch (error) {
    throw {
      message: resolveErrorMessage(error, '下单失败，请稍后重试')
    }
  }
}
