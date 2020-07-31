package com.hjg.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;

public class RedisAppTest {

    @Test
    public void basicTest() {
        RedisURI redisURI = RedisURI.Builder.redis("10.153.61.38", 8579)
                .withPassword("attCka123#mm").withDatabase(0).build();

        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        String key = "com.chinamobile.rcs.sms.phonenumber.location::@1980202";
        String value = syncCommands.get(key);
        //这里的value是String类型的JSON格式字符串，如果使用springboot配合序列化器可以序列化为Java对象

        System.out.println("value = " + value);

        connection.close();
        redisClient.shutdown();
    }
}
