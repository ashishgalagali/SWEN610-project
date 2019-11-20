package com.webcheckers.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * @author kirtanasuresh
 */
public class WebCheckersDAO {

    private static final MongoClient client = new MongoClient("localhost", 27017);
    private static final Datastore datastore = new Morphia().createDatastore(client, "WebCheckers");
    MongoDatabase db = client.getDatabase("WebCheckers");

    public WebCheckersDAO() {
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public MongoDatabase getDb() {
        return db;
    }
}
