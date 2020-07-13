package com.hjg.mongo.driver;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoUtil2 {

    private static MongoClient createMongoClient(String[] hosts, int[] ports) {

        List<ServerAddress> serverAddressList = new ArrayList<>();
        for(int i=0; i<hosts.length; i++) {
            serverAddressList.add(new ServerAddress(hosts[i], ports[i]));
        }

        String user = "mongoadmin", password = "123456", database = "admin";
        MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());

        int connectionsPerHost = 10;
        int minConnectionsPerHost = 10;
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(connectionsPerHost)
                .minConnectionsPerHost(minConnectionsPerHost)
                .build();

        MongoClient mongoClient = new MongoClient(serverAddressList, credential, options);


        return mongoClient;
    }

    public static void printBooks(String[] hosts, int[] ports, String database, String collectionName) {
        MongoClient mongoClient = MongoUtil2.createMongoClient(hosts, ports);

        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

        String author = "Jimmy";

        collection.find(Filters.eq("author", author))
                .forEach(
                        document -> System.out.println(document.toJson())
                );
    }
}
