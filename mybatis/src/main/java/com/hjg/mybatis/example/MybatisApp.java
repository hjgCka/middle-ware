package com.hjg.mybatis.example;

import com.hjg.mybatis.example.mapper.BlogMapper;
import com.hjg.mybatis.example.vo.Blog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MybatisApp {

    public static void main(String[] args) throws IOException {
        String configResource = "mybatis-config.xml";
        InputStream configInputStream = Resources.getResourceAsStream(configResource);

        String propsResource = "jdbc.properties";
        InputStream propsInputStream = Resources.getResourceAsStream(propsResource);
        Properties props = new Properties();
        props.load(propsInputStream);

        //SqlSessionFactoryBuilder随时使用和丢弃
        //SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(configInputStream, props);

        //每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享
        //回一个响应后，就关闭它。 这个关闭操作很重要
        try (SqlSession session = sqlSessionFactory.openSession()) {

            //映射器实例应该在调用它们的方法中被获取，使用完毕之后即可丢弃
            BlogMapper blogMapper = session.getMapper(BlogMapper.class);
            String id = "1";
            Blog blog = blogMapper.selectBlog(id);

            System.out.println("blog = " + blog);

            Assert.assertEquals(id, blog.getId());
        }
    }
}
