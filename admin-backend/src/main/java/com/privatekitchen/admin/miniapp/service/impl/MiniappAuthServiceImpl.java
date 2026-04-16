package com.privatekitchen.admin.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.UserDao;
import com.privatekitchen.admin.entity.User;
import com.privatekitchen.admin.miniapp.dto.MiniappLoginRequest;
import com.privatekitchen.admin.miniapp.service.MiniappAuthService;
import com.privatekitchen.admin.miniapp.vo.MiniappLoginUserVO;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class MiniappAuthServiceImpl implements MiniappAuthService {

    private final UserDao userDao;

    public MiniappAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public MiniappLoginUserVO login(MiniappLoginRequest request) {
        if (!StringUtils.hasText(request.getNickname())) {
            throw new IllegalArgumentException("未获取到微信昵称，请重新授权");
        }

        String openid = resolveOpenid(request);
        User existing = userDao.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, openid)
                .eq(User::getDelFlag, 0)
                .last("LIMIT 1"));

        if (existing != null && Integer.valueOf(1).equals(existing.getStatus()) == false) {
            throw new IllegalArgumentException("当前账号已停用，请联系管理员");
        }

        LocalDateTime now = LocalDateTime.now();
        User user = existing == null ? new User() : existing;
        user.setOpenid(openid);
        user.setNickname(request.getNickname().trim());
        user.setAvatar(StringUtils.hasText(request.getAvatar()) ? request.getAvatar().trim() : "");
        user.setPhone(existing != null ? existing.getPhone() : null);
        user.setPoints(existing != null && existing.getPoints() != null ? existing.getPoints() : 0);
        user.setMemberLevel(StringUtils.hasText(existing != null ? existing.getMemberLevel() : null)
                ? existing.getMemberLevel()
                : "普通");
        user.setStatus(1);
        user.setDelFlag(0);
        user.setUpdateTime(now);
        user.setUpdateBy("miniapp");

        if (existing == null) {
            user.setCreateTime(now);
            user.setCreateBy("miniapp");
            userDao.insert(user);
        } else {
            userDao.updateById(user);
        }

        MiniappLoginUserVO loginUser = new MiniappLoginUserVO();
        loginUser.setUserId(user.getId());
        loginUser.setOpenid(user.getOpenid());
        loginUser.setNickname(user.getNickname());
        loginUser.setAvatar(user.getAvatar());
        loginUser.setPoints(user.getPoints());
        loginUser.setMemberLevel(user.getMemberLevel());
        return loginUser;
    }

    private String resolveOpenid(MiniappLoginRequest request) {
        if (StringUtils.hasText(request.getOpenid())) {
            return request.getOpenid().trim();
        }
        if (StringUtils.hasText(request.getCode())) {
            return "mock_" + DigestUtils.md5DigestAsHex(request.getCode().trim().getBytes(StandardCharsets.UTF_8));
        }
        return "mock_" + System.currentTimeMillis();
    }
}
