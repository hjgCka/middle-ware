package com.hjg.spring;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

public class SpringTest {

    /**
     * 路径packageLocation一般不要用/开头
     * @throws IOException
     */
    @Test
    public void resourceTest() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String packageLocation = "com/hjg/mybatis/spring/example/model";
        Resource[] resources = resolver.getResources(packageLocation + "/**/*.class");

        for(Resource resource : resources) {
            //获取的文件名为Blog.class，不论路径多深。
            System.out.println(resource.getFilename());

            String path = resource.getURL().getPath();
            System.out.println(path);

            System.out.println(path.indexOf("github"));
        }
    }

    @Test
    public void resourceTest2() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String packageLocation = "com/mysql/cj/conf";
        Resource[] resources = resolver.getResources(packageLocation + "/**/*.class");

        for(Resource resource : resources) {
            //获取的文件名为Blog.class，不论路径多深。
            System.out.println(resource.getFilename());

            String path = resource.getURL().getPath();
            System.out.println(path);
        }
    }
}
