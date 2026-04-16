function requestOrderPayment(paymentParams) {
  return new Promise((resolve, reject) => {
    uni.requestPayment({
      provider: 'wxpay',
      timeStamp: paymentParams.timeStamp,
      nonceStr: paymentParams.nonceStr,
      package: paymentParams.package,
      signType: paymentParams.signType || 'RSA',
      paySign: paymentParams.paySign,
      success: resolve,
      fail: reject
    })
  })
}

export async function createMockPrepay(orderPayload) {
  await new Promise((resolve) => setTimeout(resolve, 500))

  return {
    orderNo: `OD${Date.now()}`,
    amount: orderPayload.amount,
    paymentParams: {
      timeStamp: `${Math.floor(Date.now() / 1000)}`,
      nonceStr: 'demoNonceStr2026',
      package: 'prepay_id=mock_prepay_id_replace_on_backend',
      signType: 'RSA',
      paySign: 'mockPaySignReplaceWithServerValue'
    }
  }
}

export async function payOrder(orderPayload, options = {}) {
  const { mock = true } = options
  const prepay = await createMockPrepay(orderPayload)

  if (mock) {
    await new Promise((resolve) => setTimeout(resolve, 700))
    return {
      orderNo: prepay.orderNo,
      paid: true,
      message: '模拟支付成功，已为你保留微信支付接入位置'
    }
  }

  const paymentResult = await requestOrderPayment(prepay.paymentParams)
  return {
    orderNo: prepay.orderNo,
    paid: true,
    paymentResult
  }
}
