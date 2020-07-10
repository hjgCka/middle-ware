package com.hjg.middle.ware.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class MongoApp {

    public static void main(String[] args) {

        int connectionsPerHost = 10;
        int minConnectionsPerHost = 10;

        MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
                .connectionsPerHost(connectionsPerHost)
                .minConnectionsPerHost(minConnectionsPerHost);
        MongoClientOptions mongoClientOptions = builder.build();

        // MongoDB地址列表
        String host1 = "10.153.61.38";
        int port1 = 27017;
        List<ServerAddress> serverAddresses = Arrays.asList(new ServerAddress(host1, port1));

        // 连接认证
        String username = "mongoadmin", password = "123456";
        String authenticationDb = "admin";
        MongoCredential mongoCredential = MongoCredential.createCredential(username, authenticationDb, password.toCharArray());

        //创建客户端和Factory
        MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredential, mongoClientOptions);

        //**********************开始访问collection****************

        String targetDb = "npdb", collectionName = "article";
        MongoDatabase database = mongoClient.getDatabase(targetDb);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        String author = "Jimmy";

        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };

        collection.find(Filters.eq("author", author)).forEach(printBlock);

        //关闭资源
        mongoClient.close();
    }
}
