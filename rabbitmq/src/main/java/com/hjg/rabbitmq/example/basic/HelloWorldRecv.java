package com.hjg.rabbitmq.example.basic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class HelloWorldRecv {

    private static String host = "10.153.61.37";
    private static int port = 8672;
    private static String username = "u-rabbitmq-app";
    private static String password = "IMSdev123";

    public static void main(String[] args) throws IOException, TimeoutException {
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

        //这个方法是一个同步方法，不会自己停下来，需要手动停止
        //这个方法使用的是推模式
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
