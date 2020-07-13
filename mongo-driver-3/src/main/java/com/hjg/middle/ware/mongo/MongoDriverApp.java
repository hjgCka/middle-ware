package com.hjg.middle.ware.mongo;

/**
 *
 */
public class MongoDriverApp {

    public static void main(String[] args) {

        String[] hosts = {"10.153.61.38"};
        int[] ports = {8717};
        String database = "npdb", collection = "article";

        //MongoUtil1.printBooks(hosts, ports, database, collection);

        MongoUtil2.printBooks(hosts, ports, database, collection);
    }
}
