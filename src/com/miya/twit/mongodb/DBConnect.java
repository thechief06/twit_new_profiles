package com.miya.twit.mongodb;

import com.miya.twit.keywrd.TwitProfileWrapper;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ertugrula
 */
public class DBConnect {

    public static DBCollection dbConnection() {

        try {
            // Connect to mongodb
            MongoClient mongo = new MongoClient("localhost", 27017);

            // get database 
            // if database doesn't exists, mongodb will create it for you
            DB db = mongo.getDB("TwitterDB");

            // get collection
            // if collection doesn't exists, mongodb will create it for you
            DBCollection collection = db.getCollection("gokhan");
            return collection;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        } catch (MongoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertTweet(List<TwitProfileWrapper> list) {

        DBCollection collection = dbConnection();
        if (collection != null) {
            BasicDBObject document;
            String address[];
            for (TwitProfileWrapper twit : list) {
                document = new BasicDBObject();
                document.append("text", twit.getText());
                document.append("userid", twit.getUserId());
                document.append("location", twit.getUserLocation());
                document.append("screenname", twit.getScreenName());
                document.append("username", twit.getUsername());

                collection.insert(document);
                System.out.println(collection.getCount() + " ---" + twit.getUsername() + " tweet'i yazıldı");
            }
        }

    }

    public ArrayList getTweetWithKeyWord(String keyWord) {
        ArrayList arr = new ArrayList();
        DBCollection collection = dbConnection();
        if (collection != null) {

            DBCursor cursor = collection.find();
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }

            BasicDBObject query = new BasicDBObject("text", new BasicDBObject("search", keyWord));
            //db.users.findOne({"username" : {$regex : ".*son.*"}});
            cursor = collection.find(query);

            String str;
            while (cursor.hasNext()) {
                str = cursor.curr().get("text").toString();
                arr.add(str);
            }
        }
        return arr;
    }

    public ArrayList getTwitterFindAll() {
         ArrayList arr = new ArrayList();
        DBCollection collection = dbConnection();
        DBCursor cursor = collection.find();
        try {
            String str="";
            while (cursor.hasNext()) {
               str=cursor.curr().get("screenname").toString();
               arr.add(str);
            }
        } finally {
            cursor.close();
        }
        
        return arr;

    }

    public void getTweetWithUserId(int userId) {


        DBCollection collection = dbConnection();
        if (collection != null) {

            DBCursor cursor = collection.find();
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }
            //------------------------------------

            // get documents by query
            BasicDBObject query = new BasicDBObject("userid", new BasicDBObject("$gt", userId));

            cursor = collection.find(query);
            System.out.println("twit bulundu : " + cursor.count());

//
//            /**
//             * ** Update ***
//             */
//            //update documents found by query "age > 30" with udpateObj "age = 20"
//            BasicDBObject newDocument = new BasicDBObject();
//            newDocument.put("age", 20);
//
//            BasicDBObject updateObj = new BasicDBObject();
//            updateObj.put("$set", newDocument);
//
//            collection.update(query, updateObj, false, true);
//
//            /**
//             * ** Find and display ***
//             */
//            cursor = collection.find(query);
//            System.out.println("Person with age > 40 after update --> " + cursor.count());
//
//
//            //get all again
//            cursor = collection.find();
//            try {
//                while (cursor.hasNext()) {
//                    System.out.println(cursor.next());
//                }
//            } finally {
//                cursor.close();
//            }


        }


    }
}
