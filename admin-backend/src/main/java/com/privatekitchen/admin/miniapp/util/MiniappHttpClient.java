package com.privatekitchen.admin.miniapp.util;

import com.privatekitchen.admin.miniapp.config.MiniappProperties;
import com.privatekitchen.admin.miniapp.vo.MiniappCode2SessionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class MiniappHttpClient {

    private final MiniappProperties miniappProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public MiniappHttpClient(MiniappProperties miniappProperties) {
        this.miniappProperties = miniappProperties;
    }

    public MiniappCode2SessionResponse code2Session(String jsCode) {
        if (!StringUtils.hasText(jsCode)) {
            throw new IllegalArgumentException("缺少 js_code，无法调用微信登录接口");
        }
        if (!StringUtils.hasText(miniappProperties.getAppId()) || !StringUtils.hasText(miniappProperties.getAppSecret())) {
            throw new IllegalStateException("未配置小程序 appId 或 appSecret");
        }

        URI uri = UriComponentsBuilder
                .fromHttpUrl(miniappProperties.getCode2sessionUrl())
                .queryParam("appid", miniappProperties.getAppId())
                .queryParam("secret", miniappProperties.getAppSecret())
                .queryParam("js_code", jsCode.trim())
                .queryParam("grant_type", "authorization_code")
                .build(true)
                .toUri();

        try {
            ResponseEntity<MiniappCode2SessionResponse> response = restTemplate.getForEntity(uri, MiniappCode2SessionResponse.class);
            return response.getBody();
        } catch (RestClientException error) {
            throw new IllegalStateException("调用微信 code2session 接口失败：" + error.getMessage(), error);
        }
    }
}
