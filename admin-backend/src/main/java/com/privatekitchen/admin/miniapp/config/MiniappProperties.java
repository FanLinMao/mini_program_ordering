package com.privatekitchen.admin.miniapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "miniapp")
public class MiniappProperties {

    private String appId;

    private String appSecret;

    private String code2sessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
}
