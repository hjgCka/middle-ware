package com.hjg.mybatis.spring.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    String id;
    String title;
    //可以在Java类中定义为String，但是mysql中的类型为时间类型。
    //这个貌似是Mysql原生就支持
    Date createTime;
}
