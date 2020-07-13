package com.hjg.mybatis.example.mapper;

import com.hjg.mybatis.example.vo.Blog;

public interface BlogMapper {
    Blog selectBlog(String id);
}
