/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.sentiment;

import com.google.common.io.Resources;
import com.miya.twit.mongodb.DBConnectSentiment;
import com.miya.twit.pajo.Expressions;
import com.miya.twit.pajo.TweetDBEntity;
import com.miya.twit.pajo.TweetSentimentEntity;
import com.miya.twit.utils.DataInitializer;
import com.miya.twit.utils.Utilities;
import com.mongodb.DBCollection;
import java.beans.Expression;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.xml.transform.OutputKeys;
import org.omg.IOP.Encoding;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;

/**
 *
 * @author SBT
 */
public class Main {

    public static void main(String[] args) throws IOException {

        TurkishMorphParser morphParser = TurkishMorphParser.createWithDefaults();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        TurkishSentenceParser sentenceParser = new TurkishSentenceParser(
                morphParser,
                disambiguator
        );

        File fileDir = new File("C:\\Users\\SBT\\Desktop\\twitler.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "ISO-8859-9"));
        String line;
        DataInitializer dIni = new DataInitializer();
        int counter = 0;
        while ((line = br.readLine()) != null) {
            ParseTweets parseTweet = new ParseTweets(sentenceParser, dIni.getExpressionList());
            TweetSentimentEntity TSE = parseTweet.decompositionTweet(line);
            System.out.println("counter : " + counter + " || " + TSE.getTweet());
            System.out.println(TSE.getTweetPolarityValue());
            counter++;
            System.out.println("-----");
        }
        br.close();
    }
}
