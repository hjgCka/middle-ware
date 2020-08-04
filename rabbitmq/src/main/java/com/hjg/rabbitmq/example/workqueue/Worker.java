package com.hjg.rabbitmq.example.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Worker {

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

        //设置prefetch
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        channel.queueDeclare(queueName, true, false,
                false, null);

        //缓冲消息直到我们能够使用它
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received :" + message);

            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" [x] Done");
                //手动发送消息确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        boolean autoAck = false;

        //消费者会不停运行，需要手动停止
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {});
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
