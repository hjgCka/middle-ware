package com.hjg.mybatis.spring.example.service;

import com.hjg.mybatis.spring.example.model.Blog;

public interface BlogService {

    Blog findById(String id);
}
