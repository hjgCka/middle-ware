package com.hjg.middle.ware.mongo;

import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Variable;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
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

    public AggregateIterable<Document> nativeLookUp(MongoDatabase database, String bossFileName, String taskContentFileName) {

        String bossUserCollection = "tmp_boss_subscribe_user";
        String taskContentCollection = "load_task_content";

        List<Variable<String>> variables = Arrays.asList(new Variable<>("task_phone", "$phone"));

        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(
                        Filters.expr(
                                new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$phone", "$$task_phone")),
                                        new Document("$eq", Arrays.asList("$file_name", bossFileName)),
                                        new Document("$ne", Arrays.asList("$differ_type", 3))
                                ))
                        )
                )
        );

        MongoCollection<Document> taskCollection = database.getCollection(taskContentCollection);
        AggregateIterable<Document> aggregateResult = taskCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.and(Filters.eq("file_name", taskContentFileName), Filters.ne("can_use", false))),
                        Aggregates.lookup(bossUserCollection, variables, pipeline, "boss_users")
                )
        ).allowDiskUse(true);

        return aggregateResult;
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
