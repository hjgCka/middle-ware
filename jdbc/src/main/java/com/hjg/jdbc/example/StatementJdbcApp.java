package com.hjg.jdbc.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class StatementJdbcApp {

    public static void main(String[] args) {
        String url = "jdbc:mysql://10.168.55.88:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "12345678";

        //JDK8的DriverManager使用了SPI机制，能够自动装载驱动。
        //如果不能自动装载驱动，需要指定Driver类。
        //比如使用Class.forName，使用-Djdbc.drivers，设置系统熟悉jdbc.drivers的值

        //DriverManager会遍历注册了的驱动，找到使用对应子协议的驱动

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                Statement stat = connection.createStatement()
        ) {

            String id = "1";
            ResultSet rs = stat.executeQuery("SELECT * FROM t_blog WHERE id_ = " + id);
            while (rs.next()){
                String dbId = rs.getString(1);
                String title=  rs.getString(2);
                Date createTime = rs.getDate(3);
                System.out.println("dbId = " + dbId + ", title = " + title +
                        ", createTime = " + createTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
