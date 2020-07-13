package com.hjg.mybatis.spring.example.service.impl;

import com.hjg.mybatis.spring.example.mapper.BlogMapper;
import com.hjg.mybatis.spring.example.model.Blog;
import com.hjg.mybatis.spring.example.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    @Transactional
    public Blog findById(String id) {
        return blogMapper.findById(id);
    }
}
