package databox.task;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import javafx.concurrent.Task;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class TaskListMongoDB {

    private String collectionName = null;

    private MongoDatabase db = null;

    public TaskListMongoDB(final String ip, final int port,
                           final String dbname, final String collectionName) {
        MongoClient client = new MongoClient(ip, port);
        db = client.getDatabase(dbname);
       this.collectionName = collectionName;
    }

    public void insertTaskInfo(TaskInfo entity) {

        MongoCollection<Document> collections = db.getCollection(this.collectionName);
        Document doc = new Document();
        doc.append("uid", entity.getUid());
        doc.append("lang", entity.getLang());
        doc.append("groupName", entity.getGroupName());
        doc.append("taskName", entity.getTaskName());
        doc.append("target", entity.getTarget());
        doc.append("filePath", entity.getFilePath());
        collections.insertOne(doc);
    }

    public void insertTaskInfo(List<TaskInfo> infos) {
        for(TaskInfo info : infos)
            insertTaskInfo(info);
    }

    public TaskInfo getTaskInfo(final String groupName, final String taskName) {
        MongoCollection<Document> collections = db.getCollection(this.collectionName);
        FindIterable<Document> findIterable = collections
                .find(Filters.and(Filters.eq("groupName", groupName), Filters.eq("taskName", taskName)))
                .projection(Projections.include("groupName", "taskName"));
        MongoCursor<Document> cursor = findIterable.iterator();
        TaskInfo entity = new TaskInfo();
        if(cursor.hasNext()) {
            Document doc = cursor.next();
            entity.setUid(doc.getString("uid"));
            entity.setLang(doc.getString("lang"));
            entity.setTarget(doc.getString("target"));
            entity.setGroupName(doc.getString("groupName"));
            entity.setTaskName(doc.getString("taskName"));
            entity.setFilePath(doc.getString("filePath"));
        }
        return entity;
    }

    public boolean exist(final String groupName, final String taskName) {
        MongoCollection<Document> collections = db.getCollection(this.collectionName);
        FindIterable<Document> findIterable = collections
                .find(Filters.and(Filters.eq("groupName", groupName), Filters.eq("taskName", taskName)))
                .projection(Projections.include("groupName", "taskName"));
        MongoCursor<Document> cursor = findIterable.iterator();
        return cursor.hasNext();
    }

    public List<TaskInfo> getAllTaskInfo() {
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();

        MongoCollection<Document> collections = db.getCollection(this.collectionName);
        FindIterable<Document> findIterable = collections.find();
        MongoCursor<Document> cursor = findIterable.iterator();
        while(cursor.hasNext()) {
            TaskInfo entity = new TaskInfo();
            Document doc = cursor.next();
            entity.setUid(doc.getString("uid"));
            entity.setLang(doc.getString("lang"));
            entity.setTarget(doc.getString("target"));
            entity.setGroupName(doc.getString("groupName"));
            entity.setTaskName(doc.getString("taskName"));
            entity.setFilePath(doc.getString("filePath"));
            taskInfos.add(entity);
        }
        return taskInfos;
    }
}
