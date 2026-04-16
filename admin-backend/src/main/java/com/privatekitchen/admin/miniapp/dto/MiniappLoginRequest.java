package com.privatekitchen.admin.miniapp.dto;

import lombok.Data;

@Data
public class MiniappLoginRequest {

    private String code;

    private String openid;

    private String nickname;

    private String avatar;
}
