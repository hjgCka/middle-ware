package com.hjg.mybatis.spring.example;

import com.hjg.mybatis.spring.example.config.AppConfig;
import com.hjg.mybatis.spring.example.config.JdbcProperties;
import com.hjg.mybatis.spring.example.model.Blog;
import com.hjg.mybatis.spring.example.service.BlogService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MybatisSpringApp {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(new Class[]{AppConfig.class});

        JdbcProperties jdbcProperties = applicationContext.getBean(JdbcProperties.class);
        System.out.println("jdbcProperties = " + jdbcProperties);

        BlogService blogService = applicationContext.getBean(BlogService.class);
        String id = "1";
        Blog blog = blogService.findById(id);
        System.out.println("blog = " + blog);
    }
}
