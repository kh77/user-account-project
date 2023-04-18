package com.sm.gateway.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperty {

    private String secretkey;
    private String expiryInMinute;


    public Long getExpireTime() {
        return Long.valueOf(expiryInMinute) * 60 * 1000;
    }
}
