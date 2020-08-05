package com.hjg.rabbitmq.example.subscribe;

import com.hjg.rabbitmq.example.conf.MqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class EmitLog {

    private static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MqProperties.HOST);
        factory.setPort(MqProperties.PORT);
        factory.setUsername(MqProperties.USERNAME);
        factory.setPassword(MqProperties.PASSWORD);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //声明一个fanout类型exchange，名称为logs
            channel.exchangeDeclare(EXCHANGE_NAME, MqProperties.FANOUT);

            String message = args.length < 1 ? "info: Hello world!" :
                    String.join(" ", args);

            channel.basicPublish(EXCHANGE_NAME, "",
                    null, message.getBytes(StandardCharsets.UTF_8));

            System.out.println("send :" + message);
        }
    }
}
