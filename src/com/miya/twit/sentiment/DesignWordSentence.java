/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.sentiment;

import com.miya.twit.pajo.ParsedWords;
import com.miya.twit.pajo.SentenceEntity;
import com.miya.twit.utils.Utilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import twitter4j.Tweet
import zemberek.morphology.apps.TurkishSentenceParser;

/**
 *
 * @author SBT
 */
public class DesignWordSentence {

    public SentenceEntity designPunctuationOfTweet(String tweet, TurkishSentenceParser sentenceParser) throws IOException {
        SentenceEntity sentenceEntity = new SentenceEntity();

        tweet = tweet.toLowerCase().trim();
        String[] arrToken = tweet.split(" ");
        List<ParsedWords> listParsedWord = new ArrayList<ParsedWords>();
        ParsedWords pW = null;
        ParseTwoWords PTW = new ParseTwoWords(sentenceParser);
        List<String> topHashTag = new ArrayList<String>();
        List<String> topMention = new ArrayList<String>();
        List<String> topUrl = new ArrayList<String>();

        // tweet hashtag, mention ve url parse
        for (int i = 0; i < arrToken.length; i++) { //while (tokenIterator.hasNext()) {
            // Token token = tokenIterator.next();
            if (arrToken[i].charAt(0) == '#') {
                topHashTag.add(arrToken[i]);
            } else if (arrToken[i].charAt(0) == '@') {
                topMention.add(arrToken[i]);
            } else if (arrToken[i].contains("http://") || arrToken[i].contains("https://") || arrToken[i].contains("ftp://") || arrToken[i].contains("ftps://")) {
                topUrl.add(arrToken[i]);
            } else {
                if (Character.isLetter(arrToken[i].charAt(0)) || Character.isDigit(arrToken[i].charAt(0))) {
                    pW = new ParsedWords();
                    pW = PTW.decompositionWord(arrToken[i]);
                    if (pW != null) {
                        listParsedWord.add(pW);
                    }
                    //   arrTwoWord.add(arrToken[i]);
                }
            }
        }
        sentenceEntity.setTopHashTag(topHashTag);
        sentenceEntity.setTopMention(topMention);
        sentenceEntity.setTopUrl(topUrl);
        sentenceEntity.setListParsedWords(listParsedWord);
     //  sentenceEntity.setSentenceList(fillSentence(listParsedWord));

        return sentenceEntity;
    }

    public String designPunctuationOfWord(String word) {
        for (int i = 0; i < Utilities.sentenceSeperator.length; i++) {
            // son karakter punctuation ise ve birleşik ise ayırılır
            if (String.valueOf(word.charAt(word.length() - 1)).equals(Utilities.sentenceSeperator[i])) {
                if (Utilities.sentenceSeperator.equals('.') && checkIsItWord(word)) { //word olup olmadığı kontrol edilir
                    continue;
                }
                CharSequence cArr = word.subSequence(0, word.length() - 2);
                return String.valueOf(cArr) + " " + word.charAt(word.length() - 1);
            } else if (String.valueOf(word.charAt(0)).equals(Utilities.sentenceSeperator[i])) { // ilk karakter punctuation ise ve birleşik ise ayırılır
                CharSequence cArr = word.subSequence(1, word.length() - 1);
                return String.valueOf(word.charAt(0)) + " " + String.valueOf(cArr);
            } else {
                return word;
            }
        }
        return null;
    }

    public String fillSentence(List<ParsedWords> listParsedWord) {
        String sentence = "";
        String word = "";
        List<String> sentenceList = new ArrayList<String>();

        for (int i = 0; i < listParsedWord.size(); i++) {
            word = designPunctuationOfWord(listParsedWord.get(i).getWord());

            listParsedWord.get(i).setWord(word);

            sentence += word + " ";
        }

        return sentence.trim();
    }

    // .ile biten bir word'ün word olup olmadığı chehck edilir  a.q. olmaz
    public boolean checkIsItWord(String word) {
        char[] arr = word.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (Character.isLetter(arr[i]) && arr.length > 3) {
                return true;
            }
        }

        return false;
    }
}
