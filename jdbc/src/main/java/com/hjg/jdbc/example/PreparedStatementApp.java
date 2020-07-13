package com.hjg.jdbc.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class PreparedStatementApp {

    public static void main(String[] args) {

        String url = "jdbc:mysql://10.168.55.88:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "12345678";

        //prepareStatement更好的性能和安全性。准备查询并重用它。
        //使用了MySQL5的预编译功能。但是防止SQL注入是在PreparedStatement中实现的，和服务器无关。
        String querySql = "SELECT * FROM t_blog WHERE id_ = ?";

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement stat = connection.prepareStatement(querySql)
        ) {
            connection.setAutoCommit(false);

            try {
                stat.setString(1, "1");
                ResultSet rs = stat.executeQuery();
                while (rs.next()){
                    String dbId = rs.getString(1);
                    String title=  rs.getString(2);
                    Date createTime = rs.getDate(3);
                    System.out.println("dbId = " + dbId + ", title = " + title +
                            ", createTime = " + createTime);
                }
            } catch (Exception e) {
                connection.rollback();
            }

            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
