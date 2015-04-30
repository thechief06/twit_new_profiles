package com.miya.twit.sentiment;

import com.google.gson.Gson;
import com.miya.twit.enums.WordType;
import com.miya.twit.pajo.Expressions;
import com.miya.twit.pajo.SentenceEntity;
import com.miya.twit.pajo.SubSentencesEntity;
import com.miya.twit.pajo.TweetSentimentEntity;
import com.miya.twit.pajo.TwoWordsEntity;
import com.miya.twit.utils.DataInitializer;
import com.miya.twit.utils.Utilities;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.SentenceMorphParse;
import zemberek.tokenizer.SentenceBoundaryDetector;
import zemberek.tokenizer.SimpleSentenceBoundaryDetector;
import zemberek.tokenizer.ZemberekLexer;

/**
 *
 * @author SBT
 */
public class ParseTweets {

    TurkishSentenceParser sentenceParser;
    List<Expressions> listExpression;

    public ParseTweets(TurkishSentenceParser sentenceParser, List<Expressions> listExpression) {
        this.sentenceParser = sentenceParser;
        this.listExpression = listExpression;
    }

    public TurkishSentenceParser createSentenceParser() throws IOException {
        TurkishMorphParser morphParser = TurkishMorphParser.createWithDefaults();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        sentenceParser = new TurkishSentenceParser(
                morphParser,
                disambiguator
        );
        return sentenceParser;
    }

    public TweetSentimentEntity decompositionTweet(String tweet) throws IOException {
        //tweet cümle pars'ı
        DesignWordSentence design = new DesignWordSentence();
        System.out.println("---Tweet Pars Edilmeye Başladı---(decompositionTweet)");
        
        TweetSentimentEntity TE = new TweetSentimentEntity();
        TE.setTweet(tweet);

        SentenceBoundaryDetector detector = new SimpleSentenceBoundaryDetector();
        List<String> sentences = detector.getSentences(tweet);  //zemberek'in cümle pars 'ı stop words'lerde yanlış :) karakteri gibi   

        SentenceEntity sentenceEntity = design.designPunctuationOfTweet(tweet, sentenceParser);

        List<SubSentencesEntity> listSubSentences = new ArrayList<SubSentencesEntity>();
        ParseSentences PS = new ParseSentences(sentenceParser, listExpression);

        for (String sentence : sentences) {
            SubSentencesEntity SSE = PS.decompositionSentence(sentence);
            if (SSE != null) {
                TE.setTweetPolarityValue(SSE.getSentencePolarity() + TE.getTweetPolarityValue());
                listSubSentences.add(SSE);
                System.out.println("cümle : " + sentence + " sonucu : " + SSE.getSentencePolarity());
            }
        }
        TE.setSubSentencesEntities(listSubSentences);
        TE.setTopHashTag(sentenceEntity.getTopHashTag());
        TE.setTopMention(sentenceEntity.getTopMention());
        TE.setTopUrl(sentenceEntity.getTopUrl());
        return TE;
    }

    // ponctiaction(noktalama) ile parsedword karşılaştırılıp sentence haline getirilip return edilecek
    public List<String> seperationSentences(SentenceEntity sentenceEntity) {
        for (int i = 0; i < Utilities.sentenceSeperator.length; i++) {

        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        //Seni sevmemin sebebi güzel olman değil asaletindir
        //dans hocasının bana dediğine gelll 'canım ya tangoya da kal lütfen orda yakışıklı çocuklarda var hem' asdfghjkkjhgfds
        String tweet2 = "yıkıla yıkıla gezip dolaşalım, eskiyi unut gel barışalım, gözünü gözüme değdir o zaman, kalbime dokun aşk konuşalım ?? http://t.co/gqnctpım6p";
        //  String tweet2 = "mahkeme, soma holding yönetim kurulu başkanı alp gürkan hakkında savcılığın talep ettiği yakalama kararını reddetti!! http://t.co/ftmpehkewd";
        //   String[] tweetList = new String[]{"kötüleme", "kötülemelisin", "kötülememelisin", "kötülemedi", "kötüledi", "kötülemesin", "kötülesin", "kötülememek"};
        TurkishMorphParser morphParser = TurkishMorphParser.createWithDefaults();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        TurkishSentenceParser sentenceParser = new TurkishSentenceParser(
                morphParser,
                disambiguator
        );
        DataInitializer dIni = new DataInitializer();
        ParseTweets pt = new ParseTweets(sentenceParser, dIni.getExpressionList());
        pt.decompositionTweet(tweet2);

    }
}
