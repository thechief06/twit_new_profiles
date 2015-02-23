/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.deneme;

import com.miya.twit.enums.WordType;
import com.miya.twit.mongodb.DBConnectSentiment;
import com.miya.twit.pajo.TweetDBEntity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.lexicon.Suffix;
import zemberek.morphology.parser.MorphParse;
import zemberek.morphology.parser.SentenceMorphParse;

/**
 *
 * @author SBT
 */
public class InsertWordMongoDBToFile {

    private TurkishSentenceParser sentenceParser;

    public InsertWordMongoDBToFile(TurkishSentenceParser sentenceParser) {
        this.sentenceParser = sentenceParser;
    }

    public static void main(String[] args) throws IOException {

        TurkishMorphParser morphParser = TurkishMorphParser.createWithDefaults();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        TurkishSentenceParser sentenceParser = new TurkishSentenceParser(
                morphParser,
                disambiguator
        );
        new InsertWordMongoDBToFile(sentenceParser).writeMongoDB("katliam");
        // DBConnectSentiment.getWordWithKeyWord(" dd", 1);
    }

    public void writeMongoDB(String word) throws FileNotFoundException, IOException {

        List<TweetDBEntity> list = new ArrayList<TweetDBEntity>();
        if (word.isEmpty()) {
            String filePath = "E:/sentiment/TR/negatifKokEk.txt";
            File fileDir = new File(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "ISO-8859-9")); //ISO-8859-9
            String line;
            TweetDBEntity TDB = null;
            while ((line = br.readLine()) != null) {
                TDB = new TweetDBEntity();
                TDB.setRootType(WordType.getWordTypeValue(WordType.Noun));
                TDB.setRootType(MorphParseWithPos(line));
                TDB.setPolarity(-2);
                TDB.setText(line);
                list.add(TDB);

            }
        } else {
            TweetDBEntity TDB = new TweetDBEntity();
            TDB.setRootType(WordType.getWordTypeValue(WordType.Verb));
            TDB.setRootType(MorphParseWithPos(word));
            TDB.setPolarity(-2);
            TDB.setText(word);
            list.add(TDB);
        }

        DBConnectSentiment DB = new DBConnectSentiment();
        DB.insertWord(list);
        System.out.println("DB yazma işlemi bitti");

    }

    private int MorphParseWithPos(String word) {
        SentenceMorphParse sentenceParse = sentenceParser.parse(word);
        sentenceParser.disambiguate(sentenceParse);

        for (SentenceMorphParse.Entry entry : sentenceParse) {
            System.out.println("Word = " + entry.input);
            for (MorphParse parse : entry.parses) {
                if (entry.input == parse.root) {
                    return WordType.getWordTypeValue(WordType.getWordType(parse.getPos()));
                }
            }
        }
        return 0;
    }
}
