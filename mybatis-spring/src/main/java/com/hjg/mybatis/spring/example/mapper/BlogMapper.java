package com.hjg.mybatis.spring.example.mapper;

import com.hjg.mybatis.spring.example.model.Blog;

public interface BlogMapper {

    Blog findById(String id);
}
