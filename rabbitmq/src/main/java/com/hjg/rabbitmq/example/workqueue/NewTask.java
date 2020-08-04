package com.hjg.rabbitmq.example.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NewTask {

    private static String host = "10.153.61.37";
    private static int port = 8672;
    private static String username = "u-rabbitmq-app";
    private static String password = "IMSdev123";

    /**
     * rabbitmq会按顺序将消息发给下一位消费者。每一位消费者会得到相同数目的消息。
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        String queueName = "hello";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //这个方法是幂等的，如果有就不创建，没有就创建
            channel.queueDeclare(queueName, true, false, false, null);

            //将字符串数组合并，并用指定的字符进行分隔
            String message = String.join(" ", args);

            //设置消息持久化
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("send '" + message + "'");
        }
    }
}
