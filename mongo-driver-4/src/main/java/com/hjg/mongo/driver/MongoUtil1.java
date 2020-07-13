package com.hjg.mongo.driver;

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

public class MongoUtil1 {

    private static MongoClient mongoClient = null;

    /**
     * 支持3.7以来的驱动使用的方法。
     * @return
     */
    private static MongoClient createMongoClient(String[] hosts, int[] ports) {

        List<ServerAddress> serverAddressList = new ArrayList<>();
        for(int i=0; i<hosts.length; i++) {
            serverAddressList.add(new ServerAddress(hosts[i], ports[i]));
        }

        String user = "mongoadmin", password = "123456", database = "admin";
        MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder -> builder.hosts(serverAddressList))
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient;
    }

    public static void printBooks(String[] hosts, int[] ports, String database, String collectionName) {
        MongoClient mongoClient = MongoUtil1.createMongoClient(hosts, ports);

        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

        String author = "Jimmy";

        collection.find(Filters.eq("author", author))
                .forEach(
                        document -> System.out.println(document.toJson())
        );

        mongoClient.close();
    }
}
