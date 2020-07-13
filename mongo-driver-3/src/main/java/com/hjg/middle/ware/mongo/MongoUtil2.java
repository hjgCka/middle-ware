package com.hjg.middle.ware.mongo;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 3.11.2的示例代码。
 */
public class MongoUtil2 {

    private static MongoClient getMongoClient(String[] hosts, int[] ports) {

        List<ServerAddress> serverAddresses = new ArrayList<>();
        for(int i=0; i<hosts.length; i++) {
            serverAddresses.add(new ServerAddress(hosts[i], ports[i]));
        }

        String username = "mongoadmin", password = "123456", authenticationDb = "admin";
        MongoCredential credential = MongoCredential.createCredential(username, authenticationDb, password.toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToConnectionPoolSettings(bulder -> bulder.maxSize(10).minSize(5)
                        .maxWaitQueueSize(20)
                        .maxWaitTime(5000, TimeUnit.MILLISECONDS)
                        .maxConnectionLifeTime(1000, TimeUnit.SECONDS)
                        .maxConnectionIdleTime(1000, TimeUnit.SECONDS))
                .applyToClusterSettings(builder -> builder.hosts(serverAddresses))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient;
    }

    public static void printBooks(String[] hosts, int[] ports, String databaseName, String collectionName) {
        MongoClient mongoClient = getMongoClient(hosts, ports);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Block<Document> printBlock = document -> System.out.println(document.toJson());
        String author = "Jimmy";
        collection.find(Filters.eq("author", author)).forEach(printBlock);

        //关闭资源
        mongoClient.close();
    }
}
