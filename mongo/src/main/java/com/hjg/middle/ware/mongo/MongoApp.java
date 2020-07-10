package com.hjg.middle.ware.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoApp {

    static MongoClient getMongoClient(String[] addresses) {
        int connectionsPerHost = 10;
        int minConnectionsPerHost = 10;

        MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
                .connectionsPerHost(connectionsPerHost)
                .minConnectionsPerHost(minConnectionsPerHost);
        MongoClientOptions mongoClientOptions = builder.build();

        // MongoDB地址列表
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for(String address : addresses) {
            String[] array = address.split(":");
            serverAddresses.add(new ServerAddress(array[0], Integer.valueOf(array[1])));
        }

        // 连接认证
        String username = "mongoadmin", password = "123456";
        String authenticationDb = "admin";
        MongoCredential mongoCredential = MongoCredential.createCredential(username, authenticationDb, password.toCharArray());

        //创建客户端和Factory
        MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredential, mongoClientOptions);

        return mongoClient;
    }

    public static void main(String[] args) {

        String[] addresses = new String[]{"10.153.61.38:8717"};
        MongoClient mongoClient = getMongoClient(addresses);

        String targetDb = "npdb", collectionName = "article";
        MongoDatabase database = mongoClient.getDatabase(targetDb);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        String author = "Jimmy";

        Block<Document> printBlock = document -> System.out.println(document.toJson());
        collection.find(Filters.eq("author", author)).forEach(printBlock);

        //关闭资源
        mongoClient.close();
    }
}
