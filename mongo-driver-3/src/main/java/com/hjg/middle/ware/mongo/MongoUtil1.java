package com.hjg.middle.ware.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码范例来自3.6.4版本驱动。
 */
public class MongoUtil1 {

    private static MongoClient getMongoClient(String[] hosts, int[] ports) {

        // MongoDB地址列表
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for(int i=0; i<hosts.length; i++) {
            serverAddresses.add(new ServerAddress(hosts[i], ports[i]));
        }

        // 连接认证
        String username = "mongoadmin", password = "123456", authenticationDb = "admin";
        MongoCredential mongoCredential = MongoCredential.createCredential(username, authenticationDb, password.toCharArray());

        int connectionsPerHost = 10;
        int minConnectionsPerHost = 10;
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
                .connectionsPerHost(connectionsPerHost)
                .minConnectionsPerHost(minConnectionsPerHost);
        MongoClientOptions mongoClientOptions = builder.build();

        MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredential, mongoClientOptions);

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
