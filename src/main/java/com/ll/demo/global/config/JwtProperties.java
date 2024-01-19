package com.ll.demo.global.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String SECRET_KEY;

}