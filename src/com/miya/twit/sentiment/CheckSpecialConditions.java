/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.sentiment;

import com.google.common.base.Joiner;
import com.miya.twit.enums.Polarity;
import com.miya.twit.enums.TwoWordConditionEnum;
import com.miya.twit.enums.WordConditionEnum;
import com.miya.twit.pajo.Expressions;
import com.miya.twit.pajo.ParsedWords;
import com.miya.twit.pajo.SubSentencesEntity;
import com.miya.twit.pajo.TwoWordsEntity;
import com.miya.twit.utils.Utilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author SBT
 */
public class CheckSpecialConditions {

    List<Expressions> listExpression;

    public CheckSpecialConditions(List<Expressions> listExpression) {
        this.listExpression = listExpression;
    }

    public CheckSpecialConditions() {

    }
// Cümle Bazlı Condition
    public List<TwoWordsEntity> checkExpressionConditions(String sentence, List<ParsedWords> listParsedWord, ParseTwoWords PTW) throws IOException {
        List<TwoWordsEntity> listTwoWordsEntity = new ArrayList<TwoWordsEntity>();
        TwoWordsEntity TWE = null;
        for (int i = 0; i < listExpression.size(); i++) {
            if (sentence.contains(listExpression.get(i).getExpression())) {
                TWE = new TwoWordsEntity();
                TWE.setTwoWord(listExpression.get(i).getExpression());
                TWE.setTwoWordCondition(TwoWordConditionEnum.expression);
                TWE.setTwoWordPolarity(Polarity.getPolarityValue(listExpression.get(i).getPolarity()));

                List<ParsedWords> newList = new ArrayList<ParsedWords>();
                for (int j = 0; j < listParsedWord.size(); j++) {
                    if (listExpression.get(i).getWordForWord().contains(listParsedWord.get(j).getWord())) {
                        newList.add(listParsedWord.get(j));
                    }
                }
                TWE.setPassedWord(newList);
                listTwoWordsEntity.add(TWE);
            } else {
                String sentencelastElement = listExpression.get(i).getWordForWord().get(listExpression.get(i).getWordForWord().size() - 1);
                String sentenceFullElement = Joiner.on(" ").join(listExpression.get(i).getWordForWord()).trim();
                sentenceFullElement = sentenceFullElement.substring(0, sentenceFullElement.length() - (sentencelastElement.length() + 1));

                if (sentence.contains(sentenceFullElement)) {
                    List<ParsedWords> newList = new ArrayList<ParsedWords>();
                    for (int k = 0; k < listExpression.get(i).getWordForWord().size(); k++) {
                        for (int j = 0; j < listParsedWord.size(); j++) {
                            if (listExpression.get(i).getWordForWord().get(k).equals(listParsedWord.get(j).getWord())) {
                                newList.add(listParsedWord.get(j));
                                if (k + 2 == listExpression.get(i).getWordForWord().size()) {
                                    ParsedWords pW = PTW.decompositionWord(listExpression.get(i).getWordForWord().get(k + 1)); //Deyim in son kelimesinin kökü benziyorsa alınır
                                    if (j + 1 < listParsedWord.size()) {
                                        if (pW.getPolarWord().equals(listParsedWord.get(j + 1).getPolarWord())) {
                                            TWE = new TwoWordsEntity();
                                            TWE.setTwoWord(sentenceFullElement + " " + listParsedWord.get(j));
                                            TWE.setTwoWordCondition(TwoWordConditionEnum.expression);
                                            if (i < 254) { //254 manuel bir değer insert edildiğinde arttırılıması gerek
                                                TWE.setTwoWordPolarity(Polarity.getPolarityValue(Polarity.Negative));
                                            } else {
                                                TWE.setTwoWordPolarity(Polarity.getPolarityValue(Polarity.Positive));
                                            }
                                            TWE.setPassedWord(newList);
                                            listTwoWordsEntity.add(TWE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (sentence.contains(sentenceFullElement)) {
                    List<ParsedWords> newList = new ArrayList<ParsedWords>();
                    for (int j = 0; j < listParsedWord.size(); j++) {
                        for (int k = 0; k < listExpression.get(i).getWordForWord().size(); k++) {
                            if (listExpression.get(i).getWordForWord().get(k).equals(listParsedWord.get(j).getWord())) {
                                newList.add(listParsedWord.get(j));
                                if (k + 2 == listExpression.get(i).getWordForWord().size()) {
                                    ParsedWords pW = PTW.decompositionWord(listExpression.get(i).getWordForWord().get(k + 1)); //Deyim in son kelimesinin kökü benziyorsa alınır
                                    if (j + 1 < listParsedWord.size()) {
                                        if (pW.getPolarWord().equals(listParsedWord.get(j + 1).getPolarWord())) {
                                            TWE = new TwoWordsEntity();
                                            TWE.setTwoWord(sentenceFullElement + " " + listParsedWord.get(j));
                                            TWE.setTwoWordCondition(TwoWordConditionEnum.expression);
                                            if (i < 254) {
                                                TWE.setTwoWordPolarity(Polarity.getPolarityValue(Polarity.Negative));
                                            } else {
                                                TWE.setTwoWordPolarity(Polarity.getPolarityValue(Polarity.Positive));
                                            }
                                            TWE.setPassedWord(newList);
                                            listTwoWordsEntity.add(TWE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return listTwoWordsEntity;
    }

    public TwoWordsEntity checkNeNeConditions(List<ParsedWords> listParsedWord, ParseTwoWords PW) throws IOException {
        try {
            int neOffset = -99;
            for (int i = 0; i < listParsedWord.size(); i++) {
                if (listParsedWord.get(i).getWord().equals("ne")) {
                    neOffset = i;
                }
            }

            if (neOffset != -99) {   // ne .. ne bağlacı
                CheckPointOfWord cPW = new CheckPointOfWord();
                TwoWordsEntity TWE = new TwoWordsEntity();

                if (listParsedWord.get(neOffset + 2).getWord().equals("ne") || listParsedWord.get(neOffset + 2).getWord().equals("nede")) {  // ne  # ne , ne # nede  ve ne # ne de
                    String subNe = "";
                    if (listParsedWord.get(neOffset + 2).getWord().equals("ne")) {
                        subNe = "ne";
                    } else {
                        subNe = "nede";
                    }

                    if (listParsedWord.get(neOffset + 3).getWord().equals("de")) {
                        listParsedWord.remove(neOffset + 3);
                    }
                    TWE.setTwoWord("ne " + listParsedWord.get(neOffset + 1).getWord() + " " + subNe + " " + listParsedWord.get(neOffset + 3).getWord());
                    ParsedWords parsedFirstWord = listParsedWord.get(neOffset + 1);
                    ParsedWords parsedSecondWord = listParsedWord.get(neOffset + 3);
                    TWE.setTwoWordPolarity(getTwoWordPolarity(parsedFirstWord, parsedSecondWord, TwoWordConditionEnum.naNe));
                    List<ParsedWords> newList = new ArrayList<ParsedWords>();
                    newList.add(parsedFirstWord);
                    newList.add(parsedSecondWord);
                    TWE.setPassedWord(newList);
                    TWE.setTwoWordCondition(TwoWordConditionEnum.naNe);
                    return TWE;
                } else if (listParsedWord.get(neOffset + 3).getWord().equals("ne") || listParsedWord.get(neOffset + 3).getWord().equals("nede")) {  // ne  ## ne , ne ## nede  ve ne ## ne de
                    String subNe = "";
                    if (listParsedWord.get(neOffset + 3).getWord().equals("ne")) {
                        subNe = "ne";
                    } else {
                        subNe = "nede";
                    }
                    if (listParsedWord.get(neOffset + 4).getWord().equals("de")) {
                        listParsedWord.remove(neOffset + 4);
                    }

                    if (listParsedWord.get(neOffset + 5) != null) {
                        TWE.setTwoWord("ne " + listParsedWord.get(neOffset + 1).getWord() + " " + listParsedWord.get(neOffset + 2).getWord() + " " + subNe + " " + listParsedWord.get(neOffset + 4).getWord() + " " + listParsedWord.get(neOffset + 5).getWord());

                        List<TwoWordsEntity> listTwoWordFirst = checkExpressionConditions(listParsedWord.get(neOffset + 1).getWord() + " " + listParsedWord.get(neOffset + 2).getWord(), listParsedWord, PW);
                        TwoWordsEntity twoWordFirst = listTwoWordFirst.get(0);
                        if (twoWordFirst == null) {
                            twoWordFirst = PW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(neOffset + 1), listParsedWord.get(neOffset + 2))));
                        }

                        List<TwoWordsEntity> listTwoWordSecond = checkExpressionConditions(listParsedWord.get(neOffset + 4).getWord() + " " + listParsedWord.get(neOffset + 5).getWord(), listParsedWord, PW);
                        TwoWordsEntity twoWordSecond = listTwoWordSecond.get(0);
                        if (twoWordSecond == null) {
                            twoWordSecond = PW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(neOffset + 4), listParsedWord.get(neOffset + 5))));
                        }

                        TWE.setTwoWordPolarity(getTwoTwoWordsEntityPolarity(twoWordFirst, twoWordSecond, TwoWordConditionEnum.naNe));
                        twoWordFirst.getPassedWord().addAll(twoWordSecond.getPassedWord());
                        TWE.setPassedWord(twoWordFirst.getPassedWord());
                        TWE.setTwoWordCondition(TwoWordConditionEnum.naNe);
                        return TWE;
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public TwoWordsEntity checkHemHemConditions(List<ParsedWords> listParsedWord, ParseTwoWords PW) throws IOException {
        try {
            int hemOffset = -99;
            for (int i = 0; i < listParsedWord.size(); i++) {
                if (listParsedWord.get(i).getWord().equals("hem")) {
                    hemOffset = i;
                }
            }

            if (hemOffset != -99) {   // hem .. hem bağlacı
                CheckPointOfWord cPW = new CheckPointOfWord();
                TwoWordsEntity TWE = new TwoWordsEntity();
                if (listParsedWord.get(hemOffset + 2).getWord().equals("hem") || listParsedWord.get(hemOffset + 2).getWord().equals("hemde")) {    // hem  # hem # , hem # hemde # ve hem # hem de #
                    String subHem = "";
                    if (listParsedWord.get(hemOffset + 2).getWord().equals("hem")) {
                        subHem = "hem";
                    } else {
                        subHem = "hemde";
                    }
                    if (listParsedWord.get(hemOffset + 3).getWord().equals("de")) {
                        listParsedWord.remove(hemOffset + 3);
                    }
                    TWE.setTwoWord("hem " + listParsedWord.get(hemOffset + 1).getWord() + " " + subHem + " " + listParsedWord.get(hemOffset + 3).getWord());
                    ParsedWords parsedFirstWord = listParsedWord.get(hemOffset + 1);
                    ParsedWords parsedSecondWord = listParsedWord.get(hemOffset + 3);
                    TWE.setTwoWordPolarity(getTwoWordPolarity(parsedFirstWord, parsedSecondWord, TwoWordConditionEnum.hemHem));
                    List<ParsedWords> newList = new ArrayList<ParsedWords>();
                    newList.add(parsedFirstWord);
                    newList.add(parsedSecondWord);
                    TWE.setPassedWord(newList);
                    TWE.setTwoWordCondition(TwoWordConditionEnum.hemHem);
                    return TWE;
                } else if (listParsedWord.get(hemOffset + 3).getWord().equals("hem") || listParsedWord.get(hemOffset + 3).getWord().equals("hemde")) {   // hem  ## hem ## , hem ## hemde ##  ve hem ## hem de ##
                    String subHem = "";
                    if (listParsedWord.get(hemOffset + 3).getWord().equals("hem")) {
                        subHem = "hem";
                    } else {
                        subHem = "hemde";
                    }
                    if (listParsedWord.get(hemOffset + 4).getWord().equals("de")) {
                        listParsedWord.remove(hemOffset + 4);
                    }

                    if (listParsedWord.get(hemOffset + 5) != null) { //hem hem de deyim kontrolü yapıyır yok ise normak twoword inceleme yapılır  
                        TWE.setTwoWord("hem " + listParsedWord.get(hemOffset + 1).getWord() + " " + listParsedWord.get(hemOffset + 2).getWord() + " " + subHem + " " + listParsedWord.get(hemOffset + 4).getWord() + " " + listParsedWord.get(hemOffset + 5).getWord());
                        List<TwoWordsEntity> listTwoWordFirst = checkExpressionConditions(listParsedWord.get(hemOffset + 1).getWord() + " " + listParsedWord.get(hemOffset + 2).getWord(), listParsedWord, PW);
                        TwoWordsEntity twoWordFirst = listTwoWordFirst.get(0);
                        if (twoWordFirst == null) {
                            twoWordFirst = PW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(hemOffset + 1), listParsedWord.get(hemOffset + 2))));
                        }

                        List<TwoWordsEntity> listTwoWordSecond = checkExpressionConditions(listParsedWord.get(hemOffset + 4).getWord() + " " + listParsedWord.get(hemOffset + 5).getWord(), listParsedWord, PW);
                        TwoWordsEntity twoWordSecond = listTwoWordSecond.get(0);
                        if (twoWordSecond == null) {
                            twoWordSecond = PW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(hemOffset + 4), listParsedWord.get(hemOffset + 5))));
                        }
                        TWE.setTwoWordPolarity(getTwoTwoWordsEntityPolarity(twoWordFirst, twoWordSecond, TwoWordConditionEnum.hemHem));
                        twoWordFirst.getPassedWord().addAll(twoWordSecond.getPassedWord());
                        TWE.setPassedWord(twoWordFirst.getPassedWord());
                        TWE.setTwoWordCondition(TwoWordConditionEnum.hemHem);
                        return TWE;
                    }   //hem limon satıyor hem kiraz' da kodition yazılmalı
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    //Two Word Bazlı Condition
    public Polarity checkTwoWordSpecialConditions(ParsedWords parsedWord, Polarity polarValue) {
        parsedWord.setWordCondition(WordConditionEnum.none);
        if (parsedWord.getPolarWord() != parsedWord.getWord()) {
            for (String sizSuffixe : Utilities.sizSuffixes) {   //sız,siz,suz,süz              
                if (parsedWord.getSuffixList().contains(sizSuffixe)) {
                    parsedWord.setWordCondition(WordConditionEnum.sız);
                    return Polarity.getReversePolarity(polarValue);
                }
            }
            for (String maSuffixe : Utilities.maSuffixes) { //ma me             
                if (parsedWord.getSuffixList().contains(maSuffixe)) {
                    parsedWord.setWordCondition(WordConditionEnum.ma);
                    return Polarity.getReversePolarity(polarValue);
                }
            }
            if (parsedWord.getSuffixList().contains("m")) {
                int mOffset = parsedWord.getSuffixList().indexOf("m");
                for (String miyorSuffixe : Utilities.mıyorSuffixes) { //mıyor miyor muyor müyor    
                    if (parsedWord.getSuffixList().size() >= mOffset + 2) {
                        if (parsedWord.getSuffixList().get(mOffset + 1).equals(miyorSuffixe)) {
                            parsedWord.setWordCondition(WordConditionEnum.ma);
                            return Polarity.getReversePolarity(polarValue);
                        }
                    }
                }
            }
        }
        return polarValue;
    }
    
    public TwoWordsEntity checkSpecialConditions(SubSentencesEntity SSE, ParseTwoWords PTW, List<ParsedWords> listParsedWord, List<TwoWordsEntity> listTwoWords, int index) throws IOException {
        TwoWordsEntity TWE = PTW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(index), listParsedWord.get(index + 1))));
        if (TWE != null) {
            if (TWE.getTwoWordPolarity() == 0 && TWE.getTwoWordCondition() == TwoWordConditionEnum.degil) {
                if (index - 1 > -1) {
                    TWE = PTW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(index - 1), listParsedWord.get(index + 1))));
                }
                if (TWE != null && TWE.getTwoWordPolarity() != 0) {
                    TWE.setTwoWordCondition(TwoWordConditionEnum.degil);
                }
            } else if (TWE.getTwoWordPolarity() == 0 && (TWE.getTwoWordCondition() == TwoWordConditionEnum.cokAz)) {
                if (index + 2 < listParsedWord.size()) {
                    TWE = PTW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(index), listParsedWord.get(index + 2))));
                }
                if (TWE != null && TWE.getTwoWordPolarity() != 0) {
                    TWE.setTwoWordCondition(TwoWordConditionEnum.cokAz_);
                }
            }
//            } else if (TWE.getTwoWordPolarity() == 0 && TWE.getTwoWordCondition() == TwoWordConditionEnum.ikileme) {
//                if (index + 2 < arrTwoWord.size()) {
//                    ++index;
//                    TWE = PW.decompositionTwoWord(new String[]{arrTwoWord.get(index), arrTwoWord.get(index + 1)});
//                }
//            }
        }
        return TWE;
    }

    public int checkContrastConjunctions(String word) {
        for (int i = 0; i < Utilities.contrastConjunctions.length; i++) {
            if (Utilities.contrastConjunctions[i].equals(word)) {
                return i;
            }
        }
        return 99;
    }
     
    public int getTwoTwoWordsEntityPolarity(TwoWordsEntity twoWordFirst, TwoWordsEntity twoWordSecond, TwoWordConditionEnum whichCondition) {
        if (whichCondition == TwoWordConditionEnum.naNe) {
            if (twoWordFirst.getTwoWordPolarity() > 0 && twoWordSecond.getTwoWordPolarity() > 0) {
                return -1;
            } else if (twoWordFirst.getTwoWordPolarity() >= 0 && twoWordSecond.getTwoWordPolarity() < 0) {
                return 0;
            } else if (twoWordFirst.getTwoWordPolarity() < 0 && twoWordSecond.getTwoWordPolarity() > 0) {
                return 0;
            } else if (twoWordFirst.getTwoWordPolarity() < 0 && twoWordSecond.getTwoWordPolarity() <= 0) {
                return 1;
            }
        } else if (whichCondition == TwoWordConditionEnum.hemHem) {
            return twoWordFirst.getTwoWordPolarity() + twoWordSecond.getTwoWordPolarity();
        }
        return 0;
    }

    public int getTwoWordPolarity(ParsedWords parsedFirstWord, ParsedWords parsedSecondWord, TwoWordConditionEnum whichCondition) {
        if (whichCondition == TwoWordConditionEnum.naNe) {
            if (Polarity.getPolarityValue(parsedFirstWord.getPolarType()) > 0 && Polarity.getPolarityValue(parsedSecondWord.getPolarType()) > 0) {
                return -1;
            } else if (Polarity.getPolarityValue(parsedFirstWord.getPolarType()) >= 0 && Polarity.getPolarityValue(parsedSecondWord.getPolarType()) < 0) {
                return 0;
            } else if (Polarity.getPolarityValue(parsedFirstWord.getPolarType()) < 0 && Polarity.getPolarityValue(parsedSecondWord.getPolarType()) > 0) {
                return 0;
            } else if (Polarity.getPolarityValue(parsedFirstWord.getPolarType()) < 0 && Polarity.getPolarityValue(parsedSecondWord.getPolarType()) <= 0) {
                return 1;
            }
        } else if (whichCondition == TwoWordConditionEnum.hemHem) {
            return Polarity.getPolarityValue(parsedFirstWord.getPolarType()) + Polarity.getPolarityValue(parsedSecondWord.getPolarType());
        }
        return 0;
    }
}
