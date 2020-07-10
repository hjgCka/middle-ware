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

/**
 * 代码基于3.11.2版本驱动的范例。
 * 3.11.2版本的驱动，同时支持旧的com.mongodb.MongoClient驱动。
 */
public class MongoApp2 {

    static MongoClient getMongoClient(String[] addresses) {

        List<ServerAddress> serverAddresses = new ArrayList<>();
        for(String address : addresses) {
            String[] array = address.split(":");
            serverAddresses.add(new ServerAddress(array[0], Integer.valueOf(array[1])));
        }

        String username = "mongoadmin", password = "123456", database = "admin";
        MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(serverAddresses))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

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
