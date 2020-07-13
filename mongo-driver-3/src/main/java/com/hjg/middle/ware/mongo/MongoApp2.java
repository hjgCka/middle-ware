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
 * 代码基于3.11.2版本驱动的范例。
 * 3.11.2版本的驱动，同时支持旧的com.mongodb.MongoClient驱动。
 */
public class MongoApp2 {

    static MongoClient getMongoClient(String[] hosts, int[] ports) {

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

    public static void main(String[] args) {

        String[] hosts = {"10.153.61.38"};
        int[] ports = {8717};
        MongoClient mongoClient = getMongoClient(hosts, ports);

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
