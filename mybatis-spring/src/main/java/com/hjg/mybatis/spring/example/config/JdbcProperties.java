package com.hjg.mybatis.spring.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class JdbcProperties {

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.url}")
    private String url;

    /**
     * 使用@Value设置默认值，与xml配置时用法类似。
     */
    @Value("${db.test:AAAAA}")
    private String test;
}
