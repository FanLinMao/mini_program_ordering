package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

@Data
public class MiniappLoginUserVO {

    private Long userId;

    private String openid;

    private String nickname;

    private String avatar;

    private Integer points;

    private String memberLevel;
}
