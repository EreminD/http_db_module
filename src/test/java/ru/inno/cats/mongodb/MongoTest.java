package ru.inno.cats.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MongoTest {
    public static final String MONGO_URL = "mongodb://localhost:32768/";

    static MongoClient client;

    @BeforeAll
    public static void setUp() {
        client = MongoClients.create(MONGO_URL);
    }

    @AfterAll
    public static void tearDown() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    public void iCanConnect() {
        MongoCollection<Document> cats = client.getDatabase("Cats").getCollection("cats-info");

        assertNotNull(cats);
    }

    @Test
    public void iCanQueryData() {
        MongoCollection<Document> cats = client.getDatabase("Cats").getCollection("cats-info");

        FindIterable<Document> documents = cats.find();
        documents.forEach(document -> {
            String name = document.get("name", String.class);

            Integer age = null;
            try {
                age = document.getInteger("age");
            } catch (ClassCastException ex) {
                age = 0;
            }

            System.out.println(name);
            System.out.println(age);
        });
    }

    @Test
    public void iCanAddData() {
        MongoCollection<Document> cats = client.getDatabase("Cats").getCollection("cats-info");

        Document document = new Document()
                .append("name", "Крыжик")
                .append("age", 10)
                .append("color", "red");

        cats.insertOne(document);
    }


}
