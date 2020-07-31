package com.hjg.redis.example;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class PoolRedisApp {

    public static void main(String[] args) {
        RedisURI redisURI = RedisURI.Builder.redis("10.153.61.38", 8579)
                .withPassword("attCka123#mm").withDatabase(0).build();

        RedisClient redisClient = RedisClient.create(redisURI);

        GenericObjectPool<StatefulRedisConnection<String, String>> pool = ConnectionPoolSupport
                .createGenericObjectPool(() -> redisClient.connect(), new GenericObjectPoolConfig());

        // executing work
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {

            RedisCommands<String, String> commands = connection.sync();
            commands.multi();
            commands.set("key", "value");
            commands.set("key2", "value2");
            commands.exec();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // terminating
        pool.close();
        redisClient.shutdown();
    }
}
