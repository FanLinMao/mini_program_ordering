package com.privatekitchen.admin.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.privatekitchen.admin.dao.CouponDao;
import com.privatekitchen.admin.dao.ShopSettingDao;
import com.privatekitchen.admin.dao.UserAddressDao;
import com.privatekitchen.admin.dao.UserCouponDao;
import com.privatekitchen.admin.dao.UserDao;
import com.privatekitchen.admin.entity.Coupon;
import com.privatekitchen.admin.entity.ShopSetting;
import com.privatekitchen.admin.entity.User;
import com.privatekitchen.admin.entity.UserAddress;
import com.privatekitchen.admin.entity.UserCoupon;
import com.privatekitchen.admin.miniapp.dto.MiniappAddressSaveRequest;
import com.privatekitchen.admin.miniapp.service.MiniappProfileService;
import com.privatekitchen.admin.miniapp.vo.MiniappAddressVO;
import com.privatekitchen.admin.miniapp.vo.MiniappCouponVO;
import com.privatekitchen.admin.miniapp.vo.MiniappProfileHomeVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MiniappProfileServiceImpl implements MiniappProfileService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final UserDao userDao;
    private final UserCouponDao userCouponDao;
    private final CouponDao couponDao;
    private final UserAddressDao userAddressDao;
    private final ShopSettingDao shopSettingDao;

    public MiniappProfileServiceImpl(UserDao userDao,
                                     UserCouponDao userCouponDao,
                                     CouponDao couponDao,
                                     UserAddressDao userAddressDao,
                                     ShopSettingDao shopSettingDao) {
        this.userDao = userDao;
        this.userCouponDao = userCouponDao;
        this.couponDao = couponDao;
        this.userAddressDao = userAddressDao;
        this.shopSettingDao = shopSettingDao;
    }

    @Override
    public MiniappProfileHomeVO getProfileHome(Long userId, String openid) {
        User user = getActiveUser(userId, openid);
        List<UserCoupon> coupons = listUserCoupons(user.getId());
        List<UserAddress> addresses = listUserAddresses(user.getId());
        UserAddress defaultAddress = addresses.stream()
                .filter(address -> Integer.valueOf(1).equals(address.getIsDefault()))
                .findFirst()
                .orElse(addresses.isEmpty() ? null : addresses.get(0));
        ShopSetting shopSetting = getLatestShopSetting();

        MiniappProfileHomeVO result = new MiniappProfileHomeVO();
        result.setPoints(user.getPoints() == null ? 0 : user.getPoints());
        result.setCouponCount((int) coupons.stream().filter(this::isCouponAvailable).count());
        result.setSavingsAmount(calculateSavingsAmount(coupons));
        result.setMemberLevel(StringUtils.hasText(user.getMemberLevel()) ? user.getMemberLevel() : "普通会员");
        result.setAddressSummary(defaultAddress == null ? "暂未设置收货地址" : buildFullAddress(defaultAddress));
        result.setCouponSummary(buildCouponSummary(coupons));
        result.setServiceTitle(shopSetting == null || !StringUtils.hasText(shopSetting.getShopName()) ? "私人厨房" : shopSetting.getShopName());
        result.setServiceHours(shopSetting == null || !StringUtils.hasText(shopSetting.getBusinessHours())
                ? "在线服务中，平均 30 秒响应"
                : "营业时间 " + shopSetting.getBusinessHours());
        result.setServiceNotice(shopSetting == null || !StringUtils.hasText(shopSetting.getNotice())
                ? "点完即做菜，欢迎随时咨询订单与售后问题"
                : shopSetting.getNotice());
        result.setServiceSummary(result.getServiceHours());
        result.setSettingsSummary("支付方式、通知与隐私");
        return result;
    }

    @Override
    public List<MiniappCouponVO> listCoupons(Long userId, String openid) {
        User user = getActiveUser(userId, openid);
        List<UserCoupon> userCoupons = listUserCoupons(user.getId());
        if (userCoupons.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> couponIds = userCoupons.stream()
                .map(UserCoupon::getCouponId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (couponIds.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Coupon> couponMap = couponDao.selectBatchIds(couponIds).stream()
                .filter(coupon -> Integer.valueOf(0).equals(coupon.getDelFlag()))
                .collect(Collectors.toMap(Coupon::getId, coupon -> coupon));

        return userCoupons.stream()
                .map(userCoupon -> toCouponVO(userCoupon, couponMap.get(userCoupon.getCouponId())))
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<MiniappAddressVO> listAddresses(Long userId, String openid) {
        User user = getActiveUser(userId, openid);
        return listUserAddresses(user.getId()).stream()
                .map(this::toAddressVO)
                .toList();
    }

    @Override
    public MiniappAddressVO saveAddress(MiniappAddressSaveRequest request) {
        User user = getActiveUser(request.getUserId(), request.getOpenid());
        validateAddressRequest(request);

        LocalDateTime now = LocalDateTime.now();
        boolean creating = request.getId() == null;
        UserAddress address = creating ? new UserAddress() : getOwnedAddress(request.getId(), user.getId());
        List<UserAddress> existingAddresses = creating ? listUserAddresses(user.getId()) : Collections.emptyList();
        boolean shouldSetDefault = Integer.valueOf(1).equals(request.getIsDefault()) || existingAddresses.isEmpty();

        address.setUserId(user.getId());
        address.setContactName(request.getContactName().trim());
        address.setPhone(request.getPhone().trim());
        address.setProvince(nullToEmpty(request.getProvince()));
        address.setCity(nullToEmpty(request.getCity()));
        address.setDistrict(nullToEmpty(request.getDistrict()));
        address.setDetailAddress(request.getDetailAddress().trim());
        address.setTag(StringUtils.hasText(request.getTag()) ? request.getTag().trim() : "家");
        address.setIsDefault(shouldSetDefault ? 1 : 0);
        address.setDelFlag(0);
        address.setUpdateTime(now);
        address.setUpdateBy("miniapp");

        if (creating) {
            address.setCreateTime(now);
            address.setCreateBy("miniapp");
            userAddressDao.insert(address);
        } else {
            userAddressDao.updateById(address);
        }

        if (shouldSetDefault) {
            clearOtherDefaultAddresses(user.getId(), address.getId());
        }

        return toAddressVO(getOwnedAddress(address.getId(), user.getId()));
    }

    @Override
    public MiniappAddressVO setDefaultAddress(Long addressId, Long userId, String openid) {
        User user = getActiveUser(userId, openid);
        UserAddress address = getOwnedAddress(addressId, user.getId());

        clearOtherDefaultAddresses(user.getId(), addressId);
        address.setIsDefault(1);
        address.setUpdateTime(LocalDateTime.now());
        address.setUpdateBy("miniapp");
        userAddressDao.updateById(address);
        return toAddressVO(address);
    }

    @Override
    public void deleteAddress(Long addressId, Long userId, String openid) {
        User user = getActiveUser(userId, openid);
        UserAddress address = getOwnedAddress(addressId, user.getId());
        address.setDelFlag(1);
        address.setUpdateTime(LocalDateTime.now());
        address.setUpdateBy("miniapp");
        userAddressDao.updateById(address);
    }

    private User getActiveUser(Long userId, String openid) {
        if (userId == null && !StringUtils.hasText(openid)) {
            throw new IllegalArgumentException("请先登录后再操作");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getDelFlag, 0)
                .last("LIMIT 1");

        if (userId != null) {
            wrapper.eq(User::getId, userId);
        }
        if (StringUtils.hasText(openid)) {
            wrapper.eq(User::getOpenid, openid.trim());
        }

        User user = userDao.selectOne(wrapper);
        if (user == null || !Integer.valueOf(1).equals(user.getStatus())) {
            throw new IllegalArgumentException("当前用户不存在或已停用，请重新登录");
        }
        return user;
    }

    private List<UserCoupon> listUserCoupons(Long userId) {
        return userCouponDao.selectList(new LambdaQueryWrapper<UserCoupon>()
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getDelFlag, 0)
                .orderByDesc(UserCoupon::getExpireAt)
                .orderByDesc(UserCoupon::getUpdateTime));
    }

    private List<UserAddress> listUserAddresses(Long userId) {
        return userAddressDao.selectList(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getDelFlag, 0)
                .orderByDesc(UserAddress::getIsDefault)
                .orderByDesc(UserAddress::getUpdateTime));
    }

    private ShopSetting getLatestShopSetting() {
        return shopSettingDao.selectOne(new LambdaQueryWrapper<ShopSetting>()
                .eq(ShopSetting::getDelFlag, 0)
                .orderByDesc(ShopSetting::getUpdateTime)
                .last("LIMIT 1"));
    }

    private boolean isCouponAvailable(UserCoupon userCoupon) {
        if (!"unused".equalsIgnoreCase(userCoupon.getStatus())) {
            return false;
        }
        return userCoupon.getExpireAt() == null || userCoupon.getExpireAt().isAfter(LocalDateTime.now());
    }

    private String calculateSavingsAmount(List<UserCoupon> userCoupons) {
        if (userCoupons.isEmpty()) {
            return "¥0";
        }

        List<Long> couponIds = userCoupons.stream()
                .map(UserCoupon::getCouponId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (couponIds.isEmpty()) {
            return "¥0";
        }

        Map<Long, Coupon> couponMap = couponDao.selectBatchIds(couponIds).stream()
                .filter(coupon -> Integer.valueOf(0).equals(coupon.getDelFlag()))
                .collect(Collectors.toMap(Coupon::getId, coupon -> coupon));

        BigDecimal total = userCoupons.stream()
                .filter(coupon -> "used".equalsIgnoreCase(coupon.getStatus()))
                .map(UserCoupon::getCouponId)
                .map(couponMap::get)
                .filter(Objects::nonNull)
                .map(Coupon::getDiscountValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return "¥" + total.setScale(0, RoundingMode.DOWN).toPlainString();
    }

    private String buildCouponSummary(List<UserCoupon> userCoupons) {
        long availableCount = userCoupons.stream().filter(this::isCouponAvailable).count();
        long expiringCount = userCoupons.stream()
                .filter(this::isCouponAvailable)
                .filter(coupon -> coupon.getExpireAt() != null && coupon.getExpireAt().isBefore(LocalDateTime.now().plusDays(7)))
                .count();

        if (availableCount == 0) {
            return "暂无可用优惠券";
        }
        if (expiringCount == 0) {
            return availableCount + "张可用";
        }
        return availableCount + "张可用，" + expiringCount + "张即将到期";
    }

    private MiniappCouponVO toCouponVO(UserCoupon userCoupon, Coupon coupon) {
        if (coupon == null) {
            return null;
        }

        MiniappCouponVO result = new MiniappCouponVO();
        result.setId(String.valueOf(userCoupon.getId()));
        result.setName(coupon.getName());
        result.setType(coupon.getType());
        result.setDiscountValue(coupon.getDiscountValue());
        result.setThreshold(coupon.getThreshold());
        result.setStatus(userCoupon.getStatus());
        result.setStatusText(resolveCouponStatusText(userCoupon));
        result.setExpireAt(userCoupon.getExpireAt() == null ? "长期有效" : userCoupon.getExpireAt().format(DATETIME_FORMATTER));
        return result;
    }

    private String resolveCouponStatusText(UserCoupon userCoupon) {
        if ("used".equalsIgnoreCase(userCoupon.getStatus())) {
            return "已使用";
        }
        if (userCoupon.getExpireAt() != null && userCoupon.getExpireAt().isBefore(LocalDateTime.now())) {
            return "已过期";
        }
        if ("unused".equalsIgnoreCase(userCoupon.getStatus())) {
            return "可使用";
        }
        return "不可用";
    }

    private MiniappAddressVO toAddressVO(UserAddress address) {
        MiniappAddressVO result = new MiniappAddressVO();
        result.setId(String.valueOf(address.getId()));
        result.setContactName(address.getContactName());
        result.setPhone(address.getPhone());
        result.setProvince(address.getProvince());
        result.setCity(address.getCity());
        result.setDistrict(address.getDistrict());
        result.setDetailAddress(address.getDetailAddress());
        result.setFullAddress(buildFullAddress(address));
        result.setTag(address.getTag());
        result.setIsDefault(Integer.valueOf(1).equals(address.getIsDefault()));
        return result;
    }

    private String buildFullAddress(UserAddress address) {
        return String.join(" ",
                nullToEmpty(address.getProvince()),
                nullToEmpty(address.getCity()),
                nullToEmpty(address.getDistrict()),
                nullToEmpty(address.getDetailAddress())).trim().replaceAll("\\s+", " ");
    }

    private void validateAddressRequest(MiniappAddressSaveRequest request) {
        if (!StringUtils.hasText(request.getContactName())) {
            throw new IllegalArgumentException("请填写收货人姓名");
        }
        if (!StringUtils.hasText(request.getPhone())) {
            throw new IllegalArgumentException("请填写联系电话");
        }
        if (!StringUtils.hasText(request.getDetailAddress())) {
            throw new IllegalArgumentException("请填写详细地址");
        }
    }

    private UserAddress getOwnedAddress(Long addressId, Long userId) {
        UserAddress address = userAddressDao.selectOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getId, addressId)
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getDelFlag, 0)
                .last("LIMIT 1"));
        if (address == null) {
            throw new IllegalArgumentException("地址不存在或无权操作");
        }
        return address;
    }

    private void clearOtherDefaultAddresses(Long userId, Long currentId) {
        userAddressDao.update(null, new LambdaUpdateWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getDelFlag, 0)
                .ne(currentId != null, UserAddress::getId, currentId)
                .set(UserAddress::getIsDefault, 0)
                .set(UserAddress::getUpdateTime, LocalDateTime.now())
                .set(UserAddress::getUpdateBy, "miniapp"));
    }

    private String nullToEmpty(String value) {
        return StringUtils.hasText(value) ? value.trim() : "";
    }
}
