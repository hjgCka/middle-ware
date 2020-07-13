package com.hjg.mybatis.spring.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

//不再一个个注册映射器类，而是扫描类路径来发现映射器类
@MapperScan({"com.hjg.mybatis.spring.example.mapper"})
@Configuration
@PropertySource({"classpath:/com/hjg/jdbc/jdbc.properties"})
@ComponentScan({"com.hjg.mybatis.spring.example.service.impl"})
public class AppConfig {

    /**
     * @PropertySource注解将外部属性值赋值到这个对象。
     */
    @Autowired
    Environment environment;

    /**
     * 支持@Value的占位符，从Environment及其属性源解析占位符。
     * @return
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JdbcProperties jdbcProperties() {
        return new JdbcProperties();
    }

    //=====================mybatis========================

    @Bean
    public DataSource dataSource(JdbcProperties jdbcProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcProperties.getUrl());
        config.setUsername(jdbcProperties.getUsername());
        config.setPassword(jdbcProperties.getPassword());
        /*config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");*/

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        //指定mybatis配置文件
        ClassPathResource mybatisConfigResource = new ClassPathResource("com/hjg/mybatis/mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(mybatisConfigResource);

        //指定mapper.xml文件，如果在映射器类的同目录下找不到映射器配置文件
        //mybatis配置文件也可以指定mappers标签

        //通过一个路径获取多个Resource对象
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("com/hjg/mybatis/mapper/*Mapper.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
