package com.hjg.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class BasicRecvTest {

    private String host = "10.153.61.37";
    private int port = 8672;
    private String username = "u-rabbitmq-app";
    private String password = "IMSdev123";

    @Test
    public void helloWorldTest() throws IOException, TimeoutException {
        //不像发布者发布单个消息，我们会保持对消息的监听并且将其打印出来

        String queueName = "hello";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, false, false,
                false, null);

        //缓冲消息直到我们能够使用它
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received :" + message);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
