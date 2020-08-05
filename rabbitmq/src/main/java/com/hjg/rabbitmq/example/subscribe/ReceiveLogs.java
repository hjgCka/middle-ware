package com.hjg.rabbitmq.example.subscribe;

import com.hjg.rabbitmq.example.conf.MqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {

    private static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MqProperties.HOST);
        factory.setPort(MqProperties.PORT);
        factory.setUsername(MqProperties.USERNAME);
        factory.setPassword(MqProperties.PASSWORD);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, MqProperties.FANOUT);
        //声明队列，并获得随机的队列名称。
        //该队列名称是随机的，并且断开连接时会被删除
        String queueName = channel.queueDeclare().getQueue();
        //将队列与exchange进行绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("From " + queueName + " , Received '" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
