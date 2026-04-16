import { reactive } from 'vue'

export const orderTabs = [
  { key: '', label: '全部订单' },
  { key: 'pendingPay', label: '待支付' },
  { key: 'pickup', label: '待取餐' },
  { key: 'review', label: '待评价' },
  { key: 'refund', label: '退款售后' }
]

export const mockOrders = reactive([
  {
    id: 'OD20260413001',
    title: '盐煎肉 + 番茄炒蛋',
    time: '今天 12:18',
    amount: '37.00',
    status: 'pickup',
    statusText: '待取餐',
    note: '预计 12:35 可取餐',
    shopName: '神抢手小馆 · 软件园店',
    pickupType: '到店自取',
    contactName: '用餐人小王',
    items: [
      { id: 'I1', name: '盐煎肉(不含白米饭)', count: 1, price: 22 },
      { id: 'I2', name: '番茄炒蛋', count: 1, price: 15 }
    ]
  },
  {
    id: 'OD20260413002',
    title: '鱼香肉丝 + 白米饭',
    time: '今天 11:42',
    amount: '19.00',
    status: 'pendingPay',
    statusText: '待支付',
    note: '请在 15 分钟内完成支付',
    shopName: '神抢手小馆 · 软件园店',
    pickupType: '到店自取',
    contactName: '用餐人小王',
    items: [
      { id: 'I3', name: '鱼香肉丝', count: 1, price: 17 },
      { id: 'I4', name: '白米饭', count: 1, price: 2 }
    ]
  },
  {
    id: 'OD20260412008',
    title: '泡椒鸡杂 + 冰红茶',
    time: '昨天 18:42',
    amount: '22.00',
    status: 'review',
    statusText: '待评价',
    note: '评价后可得 20 积分',
    shopName: '神抢手小馆 · 软件园店',
    pickupType: '到店自取',
    contactName: '用餐人小王',
    items: [
      { id: 'I5', name: '泡椒鸡杂', count: 1, price: 17 },
      { id: 'I6', name: '冰红茶', count: 1, price: 5 }
    ]
  },
  {
    id: 'OD20260410006',
    title: '番茄炒蛋 + 清炒时蔬',
    time: '04-10 19:03',
    amount: '28.00',
    status: 'refund',
    statusText: '退款售后',
    note: '售后处理中',
    shopName: '神抢手小馆 · 软件园店',
    pickupType: '到店自取',
    contactName: '用餐人小王',
    items: [
      { id: 'I7', name: '番茄炒蛋', count: 1, price: 15 },
      { id: 'I8', name: '清炒时蔬', count: 1, price: 13 }
    ]
  }
])

export function getOrderById(orderId) {
  return mockOrders.find((order) => order.id === orderId)
}

export function addMockOrder(order) {
  mockOrders.unshift(order)
}

export function getOrderStatusText(status) {
  const statusTextMap = {
    pendingPay: '待支付',
    pickup: '待取餐',
    review: '待评价',
    refund: '退款售后'
  }

  return statusTextMap[status] || '待支付'
}
