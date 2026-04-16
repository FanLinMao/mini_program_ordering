import { addMockOrder, getOrderStatusText } from '@/mock/orders'

export async function submitCookNowOrder(orderPayload) {
  await new Promise((resolve) => setTimeout(resolve, 600))

  const orderNo = `COOK${Date.now()}`
  const totalCount = orderPayload.items.reduce((sum, item) => sum + item.count, 0)
  const title = orderPayload.items
    .slice(0, 2)
    .map((item) => item.name)
    .join(' + ')
  const status = 'pendingPay'
  const createdOrder = {
    id: orderNo,
    title: totalCount > 2 ? `${title} 等${totalCount}件商品` : title,
    time: '刚刚下单',
    amount: Number(orderPayload.amount).toFixed(2),
    status,
    statusText: getOrderStatusText(status),
    note: '请在 15 分钟内完成支付',
    shopName: '神抢手小馆 · 软件园店',
    pickupType: '到店自取',
    contactName: '用餐人小王',
    items: orderPayload.items.map((item, index) => ({
      id: item.id || `N${index + 1}`,
      name: item.name,
      count: item.count,
      price: item.price || 0
    }))
  }

  addMockOrder(createdOrder)

  return {
    orderNo,
    created: true,
    payload: orderPayload,
    order: createdOrder,
    message: '模拟下单成功，后续可替换为后端入库接口'
  }
}
