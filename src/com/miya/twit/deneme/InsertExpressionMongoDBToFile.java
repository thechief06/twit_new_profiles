/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.deneme;

import com.miya.twit.enums.Polarity;
import com.miya.twit.enums.WordType;
import com.miya.twit.mongodb.DBConnectSentiment;
import com.miya.twit.pajo.Expressions;
import com.miya.twit.pajo.TweetDBEntity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.MorphParse;
import zemberek.morphology.parser.SentenceMorphParse;

/**
 *
 * @author SBT
 */
public class InsertExpressionMongoDBToFile {

  

    public InsertExpressionMongoDBToFile() {
      
    }

    public static void main(String[] args) throws IOException {

        new InsertExpressionMongoDBToFile().writeMongoDB();
        // DBConnectSentiment.getWordWithKeyWord(" dd", 1);
    }

    public void writeMongoDB() throws FileNotFoundException, IOException {
        String filePath = "E:\\sentiment\\TR\\deyimlerAtasözleri\\pozitifDeyimlerAtasözleri.txt";
        File fileDir = new File(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "ISO-8859-9")); //ISO-8859-9
        String line;
        List<Expressions> list = new ArrayList<Expressions>();
        Expressions exp = null;
        while ((line = br.readLine()) != null) {
            exp = new Expressions();
            exp.setExpression(line.toLowerCase());
            exp.setPolarity(Polarity.Positive);
            list.add(exp);
        }

        DBConnectSentiment DB = new DBConnectSentiment();
        DB.insertExpression(list);
        System.out.println("DB yazma işlemi bitti");
    }
}
