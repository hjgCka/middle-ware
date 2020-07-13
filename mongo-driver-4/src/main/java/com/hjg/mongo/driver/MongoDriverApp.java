package com.hjg.mongo.driver;

public class MongoDriverApp {


    public static void main(String[] args) {
        String[] hosts = {"10.153.61.38"};
        int[] ports = {8717};
        String database = "npdb", collectionName = "article";
        //MongoUtil1.printBooks(hosts, ports, database, collectionName);

        MongoUtil2.printBooks(hosts, ports, database, collectionName);
    }
}
