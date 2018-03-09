package databox.db;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

public class ObjectIdTest {

    @Test
    public void testObjectId() {
        MongoClient client = new MongoClient("localhost", 27017);
        MongoDatabase db = client.getDatabase("tasklist");
        MongoCollection<Document> collection = db.getCollection("task");
        MongoCursor<Document> cursor = collection.find().iterator();
        if(cursor.hasNext()) {
            Document doc = cursor.next();
            ObjectId objectId = doc.get("_id", ObjectId.class);
            System.out.println(objectId.toHexString());
            System.out.println(objectId.toString());
        }

    }
}
