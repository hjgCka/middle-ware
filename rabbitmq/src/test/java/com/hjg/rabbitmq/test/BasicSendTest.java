package com.hjg.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BasicSendTest {

    private String host = "10.153.61.37";
    private int port = 8672;
    private String username = "u-rabbitmq-app";
    private String password = "IMSdev123";


    @Test
    public void helloWorldTest() throws IOException, TimeoutException {
        String queueName = "hello";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //这个方法是幂等的，如果有就不创建，没有就创建
            channel.queueDeclare(queueName, false, false, false, null);

            String message = "hello world!";
            //消息内容是字节数组，所以你可以以你想要的方式来编码
            channel.basicPublish("", queueName, null, message.getBytes());
        }
    }
}
