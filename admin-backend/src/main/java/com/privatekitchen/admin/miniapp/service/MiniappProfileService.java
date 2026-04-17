package com.privatekitchen.admin.miniapp.service;

import com.privatekitchen.admin.miniapp.dto.MiniappAddressSaveRequest;
import com.privatekitchen.admin.miniapp.vo.MiniappAddressVO;
import com.privatekitchen.admin.miniapp.vo.MiniappCouponVO;
import com.privatekitchen.admin.miniapp.vo.MiniappProfileHomeVO;

import java.util.List;

public interface MiniappProfileService {

    MiniappProfileHomeVO getProfileHome(Long userId, String openid);

    List<MiniappCouponVO> listCoupons(Long userId, String openid);

    List<MiniappAddressVO> listAddresses(Long userId, String openid);

    MiniappAddressVO saveAddress(MiniappAddressSaveRequest request);

    MiniappAddressVO setDefaultAddress(Long addressId, Long userId, String openid);

    void deleteAddress(Long addressId, Long userId, String openid);
}
