package com.hjg.rabbitmq.example.route;

import com.hjg.rabbitmq.example.conf.MqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect {

    private static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        if(args.length < 1) {
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MqProperties.HOST);
        factory.setPort(MqProperties.PORT);
        factory.setUsername(MqProperties.USERNAME);
        factory.setPassword(MqProperties.PASSWORD);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个direct类型的exchange，名称为direct_logs
        channel.exchangeDeclare(EXCHANGE_NAME, MqProperties.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        //设置队列与exchange之间的bindingKey，根据参数多次设置
        for(String severity : args) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
