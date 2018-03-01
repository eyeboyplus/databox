package databox.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import databox.control.Log;
import org.bson.Document;

public class SeqLogDB {
    private MongoDatabase db = null;

    private String collectionName = null;

    public SeqLogDB(final String ip, final int port,
                    final String dbName, final String collectionName) {
        db = new MongoClient(ip, port).getDatabase(dbName);
        this.collectionName = collectionName;
    }

    public void insert(Log log) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document doc = new Document();
        doc.append("serviceName", log.getServiceName());
        doc.append("reCol", log.getRelCol());
        doc.append("createdtime", log.getCreatedTime());
        doc.append("uid", log.toUID());
        doc.append("appId", log.getAppId());
        collection.insertOne(doc);
    }
}
