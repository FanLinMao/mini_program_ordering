package com.privatekitchen.admin.miniapp.service;

import com.privatekitchen.admin.miniapp.dto.MiniappSubmitOrderRequest;
import com.privatekitchen.admin.miniapp.vo.MiniappSubmitOrderVO;

public interface MiniappOrderService {

    MiniappSubmitOrderVO submitCookNowOrder(MiniappSubmitOrderRequest request);
}
