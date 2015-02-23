/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.mongodb;

import com.miya.twit.enums.Polarity;
import com.miya.twit.keywrd.TwitProfileWrapper;
import com.miya.twit.pajo.Expressions;
import com.miya.twit.pajo.TweetDBEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SBT
 */
public class DBConnectSentiment {

    public static DBCollection dbConnection() {

        try {
            // Connect to mongodb
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB("SentimentDB");
            DBCollection collection = db.getCollection("gokhan");
            return collection;
        } catch (UnknownHostException e) {
            System.out.println("dbConnection hatası : " + e.getCause());
            e.printStackTrace();
            return null;
        } catch (MongoException e) {
            System.out.println("dbConnection hatası : " + e.getCause() + "  --  " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static DBCollection dbConnectionForExpression() {

        try {
            // Connect to mongodb
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB("SentimentExpressionDB");
            DBCollection collection = db.getCollection("gokhan");
            return collection;
        } catch (UnknownHostException e) {
            System.out.println("dbConnectionForExpression hatası : " + e.getCause());
            e.printStackTrace();
            return null;
        } catch (MongoException e) {
            System.out.println("dbConnectionForExpression hatası : " + e.getCause() + "  --  " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void insertWord(List<TweetDBEntity> list) {

        DBCollection collection = dbConnection();
        if (collection != null) {
            BasicDBObject document;

            for (TweetDBEntity twit : list) {
                document = new BasicDBObject();
                document.append("text", twit.getText());
                document.append("rootType", twit.getRootType());
                document.append("polarity", twit.getPolarity());
                collection.insert(document);
                System.out.println(collection.getCount() + " ---" + twit.getText() + " tweet'i yazıldı");
            }
        }
    }

    public void deleteWordFromMongo(String word, String polarity) {
        DBCollection collection = dbConnection();
        BasicDBObject document = new BasicDBObject();
        document.put("text", word);
        document.put("polarity", polarity); //override above value 2
        collection.remove(document);
    }

    public void insertExpression(List<Expressions> list) {

        DBCollection collection = dbConnectionForExpression();
        if (collection != null) {
            BasicDBObject document;

            for (Expressions exp : list) {
                document = new BasicDBObject();
                document.append("text", exp.getExpression());
                document.append("polarity", Polarity.getPolarityValue(exp.getPolarity()));
                collection.insert(document);
                System.out.println(collection.getCount() + "---------    " + Polarity.getPolarityValue(exp.getPolarity()) + " ---" + exp.getExpression() + " Deyim'i yazıldı");
            }
        }
    }

    public static Polarity getPolarityWithWordType(String polarWord, int wordTypeValue) {
        try {
            DBCollection collection = dbConnection();
            if (collection != null) {
                DBCursor cursor = collection.find();
                BasicDBObject query = new BasicDBObject();

                query.put("text", polarWord);
          //  query.put("rootType", wordTypeValue);

                //db.users.findOne({"username" : {$regex : ".*son.*"}});
                cursor = collection.find(query);
                int polarValue;
                while (cursor.hasNext()) {
                    polarValue = Integer.parseInt(cursor.next().get("polarity").toString());
                   // System.out.println(polarValue);
                    return Polarity.getPolarity(polarValue);
                }
            }
        } catch (MongoException e) {
            System.out.println("dbConnectionForExpression hatası : " + e.getCause() + "  --  " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return Polarity.getPolarity(0);

    }

    public static List<Expressions> getPolarityOfExpression(String polarWord) {
        DBCollection collection = dbConnectionForExpression();
        if (collection != null) {
            DBCursor cursor = collection.find();
            BasicDBObject query = new BasicDBObject();

            query.put("text", polarWord);
          //  query.put("rootType", wordTypeValue);

            //db.users.findOne({"username" : {$regex : ".*son.*"}});
            cursor = collection.find(query);
            int polarValue;
            List<Expressions> listExpression = new ArrayList<Expressions>();
            Expressions exp = null;
            while (cursor.hasNext()) {
                exp = new Expressions();
                exp.setExpression(cursor.next().get("text").toString());
                exp.setPolarity(Polarity.getPolarity(Integer.parseInt(cursor.next().get("polarity").toString())));
                listExpression.add(exp);
            }
            return listExpression;
        }
        return null;
    }

    public ArrayList getTwitterFindAll() {
        ArrayList arr = new ArrayList();
        DBCollection collection = dbConnection();
        DBCursor cursor = collection.find();
        try {
            String str = "";
            while (cursor.hasNext()) {
                str = cursor.curr().get("text").toString();
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
