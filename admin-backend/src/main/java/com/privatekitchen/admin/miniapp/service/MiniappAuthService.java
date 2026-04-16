package com.privatekitchen.admin.miniapp.service;

import com.privatekitchen.admin.miniapp.dto.MiniappLoginRequest;
import com.privatekitchen.admin.miniapp.vo.MiniappLoginUserVO;

public interface MiniappAuthService {

    MiniappLoginUserVO login(MiniappLoginRequest request);
}
