/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.sentiment;

import com.miya.twit.enums.Polarity;
import com.miya.twit.enums.TwoWordConditionEnum;
import com.miya.twit.enums.WordType;
import com.miya.twit.pajo.ParsedWords;
import com.miya.twit.pajo.TwoWordsEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import zemberek.core.turkish.PrimaryPos;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.MorphParse;
import zemberek.morphology.parser.SentenceMorphParse;

/**
 *
 * @author SBT
 */
public class ParseTwoWords {

    TurkishSentenceParser sentenceParser;

    public ParseTwoWords(TurkishSentenceParser sentenceParser) {
        this.sentenceParser = sentenceParser;
    }

    CheckPointOfWord cPW = new CheckPointOfWord();

    public TwoWordsEntity decompositionTwoWord(List<ParsedWords> listTwoParsedWord) throws IOException {
        TwoWordsEntity TWE = new TwoWordsEntity();
      
        ParsedWords type = null;
        if (listTwoParsedWord.get(1) == null) {         
            TWE.setPassedWord(listTwoParsedWord);
            TWE.setTwoWordCondition(TwoWordConditionEnum.none);
            TWE.setTwoWord(listTwoParsedWord.get(0).getWord());
            TWE.setTwoWordPolarity(Polarity.getPolarityValue(listTwoParsedWord.get(0).getPolarType()));
            return TWE;
        }else{
              TWE.setTwoWord(listTwoParsedWord.get(0).getWord() + " " + listTwoParsedWord.get(1).getWord());
        }

        if (listTwoParsedWord.get(0).equals(listTwoParsedWord.get(1))) { //ikileme  Kontrolü
            TWE.setTwoWordCondition(TwoWordConditionEnum.ikileme);
            TWE.setTwoWordPolarity(0);
            return TWE;
        }

        int twoWordPolarity = viewedTwoWord(listTwoParsedWord);
        TWE.setTwoWordPolarity(twoWordPolarity);
        TWE.setTwoWordCondition(TwoWordConditionEnum.none);
        if (listTwoParsedWord.get(0).getWord().equals("çok")) {  //çok ve az olayı
            TWE.setTwoWordCondition(TwoWordConditionEnum.cokAz);
            if (TWE.getTwoWordPolarity() > 0) {
                TWE.setTwoWordPolarity(twoWordPolarity + 1);
            } else if (TWE.getTwoWordPolarity() < 0) {
                TWE.setTwoWordPolarity(twoWordPolarity - 1);
            } else {
                TWE.setTwoWordPolarity(0);
            }
        }
        if (listTwoParsedWord.get(0).getWord().equals("az")) {
            TWE.setTwoWordCondition(TwoWordConditionEnum.cokAz);
            if (TWE.getTwoWordPolarity() > 0) {
                TWE.setTwoWordPolarity(twoWordPolarity - 1);
            } else if (TWE.getTwoWordPolarity() < 0) {
                TWE.setTwoWordPolarity(twoWordPolarity + 1);
            } else {
                TWE.setTwoWordPolarity(0);
            }
        }
        if (listTwoParsedWord.get(1).getPolarWord().equals("degil") || listTwoParsedWord.get(1).getPolarWord().equals("değil")) { //değil olumsuzluk kontrolü yapıldı
            TWE.setTwoWordCondition(TwoWordConditionEnum.degil);
            if (Polarity.getPolarityValue(listTwoParsedWord.get(0).getPolarType()) > 0) {
                TWE.setTwoWordPolarity(- 1);
            } else if (Polarity.getPolarityValue(listTwoParsedWord.get(0).getPolarType()) < 0) {
                TWE.setTwoWordPolarity(1);
            } else {
                TWE.setTwoWordPolarity(0);
            }
        }

        TWE.setPassedWord(listTwoParsedWord);
        return TWE;
    }

    public  ParsedWords decompositionWord(String word) throws IOException {
        ParsedWords type = new ParsedWords();
        type.setWord(word);
        if (!setWordType(type)) { //type.wordType ve type.polarWord belirlenir
            return null;
        }
        type.setPolarType(cPW.getPointOfWord(type));
        return type;
    }

    public int viewedTwoWord(List<ParsedWords> listTwoParsed) throws IOException {
        for (int i = 0; i < listTwoParsed.size(); i++) {

            if (listTwoParsed.get(0).getWordType() == WordType.Adjective && listTwoParsed.get(1).getWordType() == WordType.Noun) {
                return Polarity.getPolarityValue(listTwoParsed.get(0).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Adjective && listTwoParsed.get(1).getWordType() == WordType.Verb) {
                return Polarity.getPolarityValue(listTwoParsed.get(0).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Adverb && listTwoParsed.get(1).getWordType() == WordType.Noun) {
                return Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Adverb && listTwoParsed.get(1).getWordType() == WordType.Verb) {
                return Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Unknown && listTwoParsed.get(1).getWordType() == WordType.Verb) {
                return Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Unknown && listTwoParsed.get(1).getWordType() == WordType.Noun) {
                return Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Unknown && listTwoParsed.get(1).getWordType() == WordType.Postpositive) {
                return Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Unknown && listTwoParsed.get(1).getWordType() == WordType.Adjective) {
                return Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            } else if (listTwoParsed.get(0).getWordType() == WordType.Unknown && listTwoParsed.get(1).getWordType() == WordType.Adverb) {
                return Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            } else {
                return Polarity.getPolarityValue(listTwoParsed.get(0).getPolarType()) + Polarity.getPolarityValue(listTwoParsed.get(1).getPolarType());
            }
        }
        return 0;
    }

    public boolean setWordType(ParsedWords type) throws IOException {
        try {
            SentenceMorphParse sentenceParse = sentenceParser.parse(type.getWord());
            sentenceParser.disambiguate(sentenceParse);
            return MorphParseWithPos(sentenceParse, type);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean MorphParseWithPos(SentenceMorphParse sentenceParse, ParsedWords type) {
        for (SentenceMorphParse.Entry entry : sentenceParse) {
            int count = 0;
            if (entry.parses != null) {
                for (MorphParse parse : entry.parses) {  //ilk kökün tipi alınır
                    if (parse.getPos() == PrimaryPos.Punctuation) {
                        return false;
                    }
                    List<String> arrSuffix = new ArrayList<String>();
                    for (MorphParse.SuffixData suffix : parse.getSuffixDataList()) {
                        if (suffix.surface.trim().length() > 0) {
                            arrSuffix.add(suffix.surface);
                        }
                    }
                    type.setSuffixList(arrSuffix);

                    if (count == 0) {
                        type.setWordType(WordType.getWordType(parse.getPos()));
                        type.setPolarWord(parse.root);
                    }
                    if (entry.input.equals(parse.root)) {
                        type.setWordType(WordType.getWordType(parse.getPos()));
                        type.setPolarWord(parse.root);
                        return true;
                    }
                    count++;
                }
                return true;
            }
        }
        return false;
    }
}
