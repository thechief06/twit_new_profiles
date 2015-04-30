/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.sentiment;

import com.miya.twit.enums.Polarity;
import com.miya.twit.enums.TwoWordConditionEnum;
import com.miya.twit.enums.WordConditionEnum;
import com.miya.twit.pajo.ParsedWords;
import com.miya.twit.enums.WordType;
import com.miya.twit.pajo.Expressions;
import com.miya.twit.pajo.SubSentencesEntity;
import com.miya.twit.pajo.TwoWordsEntity;
import com.miya.twit.utils.Utilities;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import zemberek.core.turkish.PrimaryPos;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.MorphParse;
import zemberek.morphology.parser.SentenceMorphParse;
import zemberek.tokenizer.SentenceBoundaryDetector;
import zemberek.tokenizer.SimpleSentenceBoundaryDetector;
import zemberek.tokenizer.ZemberekLexer;
import org.antlr.v4.runtime.Token;

/**
 * @author SBT
 */
public class ParseSentences {

    TurkishSentenceParser sentenceParser;
    List<Expressions> listExpression;

    public ParseSentences(TurkishSentenceParser sentenceParser, List<Expressions> listExpression) {
        this.sentenceParser = sentenceParser;
        this.listExpression = listExpression;
        CSC = new CheckSpecialConditions(listExpression);
    }

    CheckPointOfWord cPW = new CheckPointOfWord();
    CheckSpecialConditions CSC = null;
    public List<TwoWordsEntity> listTwoWords = null;

    public SubSentencesEntity decompositionSentence(String sentence) throws IOException {
        try {
            // burda token yanlış çalışıyor
           /*  ZemberekLexer lexer = new ZemberekLexer();
            Iterator<Token> tokenIterator = lexer.getTokenIterator(sentence); 
           */
            String[] arrToken = sentence.split(" ");
            List<ParsedWords> listParsedWord = new ArrayList<ParsedWords>();
            ParsedWords pW = null;
            ParseTwoWords PTW = new ParseTwoWords(sentenceParser);

            DesignWordSentence DWS = new DesignWordSentence();
            sentence = DWS.fillSentence(listParsedWord);
            SubSentencesEntity SSEFirst = null;
            SubSentencesEntity SSESecond = null;
            int offsetOfConjunctions = 0;
            for (int i = 0; i < listParsedWord.size(); i++) { //ama,fakat bağlaçları
                int contrastConjunctionsOffset = CSC.checkContrastConjunctions(listParsedWord.get(i).getWord());
                if (contrastConjunctionsOffset != 99) {
                    offsetOfConjunctions = i;
                    String firstSubSentence = "";
                    String secondSubSentence = "";
                    List<ParsedWords> firstSubSentenceList = new ArrayList<ParsedWords>();
                    List<ParsedWords> secondSubSentenceList = new ArrayList<ParsedWords>();
                    firstSubSentenceList = listParsedWord.subList(0, i);
                    firstSubSentence = DWS.fillSentence(firstSubSentenceList);
                    secondSubSentenceList = listParsedWord.subList(i + 1, listParsedWord.size());
                    secondSubSentence = DWS.fillSentence(secondSubSentenceList);

                    firstSubSentence = firstSubSentence.trim();
                    secondSubSentence = secondSubSentence.trim();
                    SSEFirst = parseSubSentence(firstSubSentenceList, firstSubSentence);
                    SSESecond = parseSubSentence(secondSubSentenceList, secondSubSentence);

                    SSEFirst.setSubSentence(SSEFirst.getSubSentence() + " " + Utilities.contrastConjunctions[contrastConjunctionsOffset] + " " + SSESecond.getSubSentence());
                    SSEFirst.getTwoWordEntity().addAll(SSESecond.getTwoWordEntity());
                    SSEFirst.setTwoWordEntity(SSEFirst.getTwoWordEntity());
                    if (SSESecond.getSentencePolarity() > 0) {// ikinci cümle olumlu
                        SSEFirst.setSentencePolarity(SSEFirst.getSentencePolarity() + SSESecond.getSentencePolarity() + 1);
                      //  SSEFirst.setTopHashTag(topHashTag);
                        return SSEFirst;
                    } else if (SSESecond.getSentencePolarity() < 0) {
                        SSEFirst.setSentencePolarity(SSEFirst.getSentencePolarity() + SSESecond.getSentencePolarity() - 1);
                    //    SSEFirst.setTopHashTag(topHashTag);
                        return SSEFirst;
                    } else {
                        SSEFirst.setSentencePolarity(SSEFirst.getSentencePolarity() + SSESecond.getSentencePolarity());
                     //   SSEFirst.setTopHashTag(topHashTag);
                        return SSEFirst;
                    }
                }
            }
            SubSentencesEntity SSE = parseSubSentence(listParsedWord, sentence);
//            SSE.setTopHashTag(topHashTag);
//            SSE.setTopMention(topMention);
//            SSE.setTopUrl(topUrl);

            return SSE;
        } catch (Exception ex) {
            System.out.println("ParseSentence Hatası : " + ex.getMessage() + " -- " + ex.fillInStackTrace() + " -- " + ex.fillInStackTrace().getMessage());
            return null;
        }
    }    

    public SubSentencesEntity parseSubSentence(List<ParsedWords> listParsedWord, String subSentence) throws IOException {
        ParseTwoWords PTW = new ParseTwoWords(sentenceParser);
        SubSentencesEntity SSE = new SubSentencesEntity();
        SSE.setSubSentence(subSentence);

        listTwoWords = new ArrayList<TwoWordsEntity>();

        //ne ne bağlacı kontrol
        TwoWordsEntity TWENeNe = CSC.checkNeNeConditions(listParsedWord, PTW);  
        if (TWENeNe != null) { //Check ne..ne bağlacı
            SSE.setSentencePolarity(TWENeNe.getTwoWordPolarity());
            listTwoWords.add(TWENeNe);
            String[] neNeArr = TWENeNe.getTwoWord().split(" ");
            for (int i = 0; i < listParsedWord.size(); i++) {
                for (int j = 0; j < neNeArr.length; j++) {
                    if (listParsedWord.get(i).getWord().equals(neNeArr[j])) {
                        listParsedWord.remove(i);
                    }
                }
            }
        }
       //hem hem bağlacı kontrol
        TwoWordsEntity TWEHemHem = CSC.checkHemHemConditions(listParsedWord, PTW);  
        if (TWEHemHem != null) { //Check ne..ne bağlacı
            SSE.setSentencePolarity(TWEHemHem.getTwoWordPolarity());
            listTwoWords.add(TWEHemHem);
            String[] hemHemArr = TWEHemHem.getTwoWord().split(" ");
            for (int i = 0; i < listParsedWord.size(); i++) {
                for (int j = 0; j < hemHemArr.length; j++) {
                    if (listParsedWord.get(i).getWord().equals(hemHemArr[j])) {
                        listParsedWord.remove(i);
                    }
                }
            }
        }
        // Deyim Atasözü Kontrolü
        List<TwoWordsEntity> listExpressionTwoWords = CSC.checkExpressionConditions(subSentence, listParsedWord, PTW);  //deyimlerin kontrolü yapılır

        if (!listExpressionTwoWords.isEmpty()) {
            for (TwoWordsEntity TWE : listExpressionTwoWords) {
                SSE.setSentencePolarity(TWE.getTwoWordPolarity() + SSE.getSentencePolarity());
                listTwoWords.add(TWE);
                for (int i = 0; i < TWE.getPassedWord().size(); i++) {
                    if (listParsedWord.contains(TWE.getPassedWord().get(i))) {
                        listParsedWord.remove(TWE.getPassedWord().get(i));
                    }
                }
            }
        }
        boolean isGoto = false;
        int twoWordPolarity = 0;
        TwoWordsEntity TWEFirst = null;
        TwoWordsEntity TWESecond = null;
        if (listParsedWord.size() - 1 > 0) {
            for (int i = 0; i < listParsedWord.size() - 1; i++) {
                do {
                    TWEFirst = CSC.checkSpecialConditions(SSE, PTW, listParsedWord, listTwoWords, i);
                    if (TWEFirst == null) {
                        if (listParsedWord.size() - 1 > i) {
                            isGoto = true;
                            break;
                        } else {
                            SSE.setTwoWordEntity(listTwoWords);
                            return SSE;
                        }
                    }
                    if (TWEFirst.getTwoWordCondition() != null && TWEFirst.getTwoWordCondition() == TwoWordConditionEnum.ikileme) {
                        ++i;
                    }
                    if (TWEFirst.getTwoWordCondition() == TwoWordConditionEnum.cokAz_) {
                        ++i;
                    }
                } while (TWEFirst.getTwoWordCondition() != null && TWEFirst.getTwoWordCondition() == TwoWordConditionEnum.ikileme);
                if (isGoto) {
                    isGoto = false;
                    continue;
                }
                twoWordPolarity = 0;
                if (i < listParsedWord.size() - 2) { // array offset tek sayı olduğu zaman  çiftlemeler tamamlandıktan sonra son TWEFirst sorun olur mu?
                    ++i;
                    do {
                        TWESecond = CSC.checkSpecialConditions(SSE, PTW, listParsedWord, listTwoWords, i);
                        if (TWESecond == null) {
                            if (listParsedWord.size() - 1 > i) {
                                isGoto = true;
                                break;
                            } else {
                                SSE.setTwoWordEntity(listTwoWords);
                                return SSE;
                            }
                        }
                        if (TWESecond.getTwoWordCondition() != null && TWESecond.getTwoWordCondition() == TwoWordConditionEnum.ikileme) {
                            ++i;
                        }
                        if (TWESecond.getTwoWordCondition() == TwoWordConditionEnum.cokAz_) {
                            ++i;
                        }
                    } while (TWESecond.getTwoWordCondition() != null && TWESecond.getTwoWordCondition() == TwoWordConditionEnum.ikileme);
                    if (isGoto) {
                        isGoto = false;
                        continue;
                    }
                    if (TWEFirst.getTwoWordCondition() == TwoWordConditionEnum.none && TWESecond.getTwoWordCondition() == TwoWordConditionEnum.none) {
                        if (TWEFirst.getTwoWordPolarity() != 0 && TWESecond.getTwoWordPolarity() != 0) {
                            if (TWEFirst.getPassedWord().get(0).getPolarType() == Polarity.Neutral && TWEFirst.getPassedWord().get(1).getPolarType() != Polarity.Neutral
                                    && TWESecond.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWESecond.getPassedWord().get(1).getPolarType() == Polarity.Neutral) {   // hızlı SUÇLAMA yapıldı  (polarity ortada)
                                String[] arrFirst = TWEFirst.getTwoWord().split(" ");
                                int secondPolarity = 0;
                                if (Polarity.getPolarityValue(TWESecond.getPassedWord().get(0).getPolarType()) > 0 && TWESecond.getPassedWord().get(1).getPolarType() == Polarity.Neutral && TWESecond.getPassedWord().get(1).getWordCondition() != WordConditionEnum.none) { //İYİ etkilemedi
                                    secondPolarity = -1;
                                } else if (Polarity.getPolarityValue(TWESecond.getPassedWord().get(0).getPolarType()) < 0 && TWESecond.getPassedWord().get(1).getPolarType() == Polarity.Neutral && TWESecond.getPassedWord().get(1).getWordCondition() != WordConditionEnum.none) { //KÖTÜ etkilemedi
                                    secondPolarity = 1;
                                } else {
                                    secondPolarity = TWESecond.getTwoWordPolarity();
                                }
                                twoWordPolarity = TWEFirst.getTwoWordPolarity() + secondPolarity - Polarity.getPolarityValue(TWEFirst.getPassedWord().get(1).getPolarType());
                                SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());

                                TWESecond.setTwoWord(arrFirst[0] + " " + TWESecond.getTwoWord());
                                TWESecond.getPassedWord().add(TWEFirst.getPassedWord().get(0));
                                TWESecond.setPassedWord(TWESecond.getPassedWord());
                                TWESecond.setTwoWordPolarity(twoWordPolarity);

                                listTwoWords.add(TWESecond);

                            } else if (TWEFirst.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWEFirst.getPassedWord().get(1).getPolarType() != Polarity.Neutral
                                    && TWESecond.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWESecond.getPassedWord().get(1).getPolarType() == Polarity.Neutral) {  //KÖTÜ SUÇLAMA yapıldı (polarity başta ve ortada)
                                String[] arrFirst = TWESecond.getTwoWord().split(" ");
                                twoWordPolarity = TWEFirst.getTwoWordPolarity() + TWESecond.getTwoWordPolarity() - Polarity.getPolarityValue(TWEFirst.getPassedWord().get(1).getPolarType());
                                SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());

                                TWEFirst.setTwoWord(TWEFirst.getTwoWord() + " " + arrFirst[1]);
                                TWEFirst.getPassedWord().add(TWESecond.getPassedWord().get(1));
                                TWEFirst.setPassedWord(TWEFirst.getPassedWord());
                                TWEFirst.setTwoWordPolarity(twoWordPolarity);
                                listTwoWords.add(TWEFirst);

                            } else if (TWEFirst.getPassedWord().get(0).getPolarType() == Polarity.Neutral && TWEFirst.getPassedWord().get(1).getPolarType() != Polarity.Neutral
                                    && TWESecond.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWESecond.getPassedWord().get(1).getPolarType() != Polarity.Neutral) {   //o KÖTÜ SUÇLAMAYDI (polarity ortada ve sonda)
                                String[] arrFirst = TWEFirst.getTwoWord().split(" ");
                                twoWordPolarity = TWEFirst.getTwoWordPolarity() + TWESecond.getTwoWordPolarity() - Polarity.getPolarityValue(TWEFirst.getPassedWord().get(1).getPolarType());
                                SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());

                                TWESecond.setTwoWord(arrFirst[0] + " " + TWESecond.getTwoWord());
                                TWESecond.getPassedWord().add(TWEFirst.getPassedWord().get(0));
                                TWESecond.setPassedWord(TWESecond.getPassedWord());
                                TWESecond.setTwoWordPolarity(twoWordPolarity);
                                listTwoWords.add(TWESecond);

                            } else if (TWEFirst.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWEFirst.getPassedWord().get(1).getPolarType() == Polarity.Neutral
                                    && TWESecond.getPassedWord().get(0).getPolarType() == Polarity.Neutral && TWESecond.getPassedWord().get(1).getPolarType() != Polarity.Neutral) { //   KÖTÜ bir SUÇLAMA ( polarity başta ve sonda)
                                String[] arrFirst = TWEFirst.getTwoWord().split(" ");
                                twoWordPolarity = TWEFirst.getTwoWordPolarity() + TWESecond.getTwoWordPolarity();
                                SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());

                                TWESecond.setTwoWord(arrFirst[0] + " " + TWESecond.getTwoWord());
                                TWESecond.getPassedWord().add(TWEFirst.getPassedWord().get(0));
                                TWESecond.setPassedWord(TWESecond.getPassedWord());
                                TWESecond.setTwoWordPolarity(twoWordPolarity);

                                listTwoWords.add(TWESecond);

                            } else if (TWEFirst.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWEFirst.getPassedWord().get(1).getPolarType() != Polarity.Neutral
                                    && TWESecond.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWESecond.getPassedWord().get(1).getPolarType() != Polarity.Neutral) {   //KÖTÜ DENGESİZ SUÇLAMA (polarity her yerde)
                                String[] arrFirst = TWEFirst.getTwoWord().split(" ");
                                twoWordPolarity = TWEFirst.getTwoWordPolarity() + TWESecond.getTwoWordPolarity() - Polarity.getPolarityValue(TWEFirst.getPassedWord().get(1).getPolarType());
                                SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());

                                TWESecond.setTwoWord(arrFirst[0] + " " + TWESecond.getTwoWord());
                                TWESecond.getPassedWord().add(TWEFirst.getPassedWord().get(0));
                                TWESecond.setPassedWord(TWESecond.getPassedWord());
                                TWESecond.setTwoWordPolarity(twoWordPolarity);
                                listTwoWords.add(TWESecond);

                            }
                        } else {
                            if (TWEFirst.getPassedWord().get(0).getPolarType() == Polarity.Neutral && TWEFirst.getPassedWord().get(1).getPolarType() == Polarity.Neutral
                                    && TWESecond.getPassedWord().get(0).getPolarType() == Polarity.Neutral && TWESecond.getPassedWord().get(1).getPolarType() == Polarity.Neutral) {    //bugün hava normal
                                listTwoWords.add(TWEFirst);
                                listTwoWords.add(TWESecond);
                                SSE.setSentencePolarity(TWESecond.getTwoWordPolarity() + TWEFirst.getTwoWordPolarity() + SSE.getSentencePolarity()); //zaten o gelecek
                            } else if (TWEFirst.getPassedWord().get(0).getPolarType() != Polarity.Neutral && TWEFirst.getPassedWord().get(1).getPolarType() == Polarity.Neutral
                                    && TWESecond.getPassedWord().get(0).getPolarType() == Polarity.Neutral && TWESecond.getPassedWord().get(1).getPolarType() == Polarity.Neutral) {  //KÖTÜ bir başlangıç
                                listTwoWords.add(TWEFirst);
                                listTwoWords.add(TWESecond);
                                twoWordPolarity = TWESecond.getTwoWordPolarity() + TWEFirst.getTwoWordPolarity();
                                SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());
                            } else {  // bugün hava GÜZEL görünüyor'da i'değişmeden yeni ikili oluşturulacak eski "bugün hava GÜZEL" yeni oluşacak  "hava GÜZEL görünüyor"
                                --i;
                            }
                        }
                    } else if (TWESecond.getTwoWordCondition() == TwoWordConditionEnum.degil) { //çok İYİ değil || çok KÖTÜ değil
                        String[] arrFirst = TWEFirst.getTwoWord().split(" ");
                        twoWordPolarity = TWESecond.getTwoWordPolarity();
                        SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());
                        TWESecond.setTwoWord(arrFirst[0] + " " + TWESecond.getTwoWord());
                        TWESecond.getPassedWord().add(TWEFirst.getPassedWord().get(0));
                        TWESecond.setPassedWord(TWESecond.getPassedWord());
                        TWESecond.setTwoWordPolarity(twoWordPolarity);
                        listTwoWords.add(TWESecond);

                    } else if (TWEFirst.getTwoWordCondition() == TwoWordConditionEnum.cokAz || TWEFirst.getTwoWordCondition() == TwoWordConditionEnum.cokAz_) {
                        String[] arrFirst = TWEFirst.getTwoWord().split(" ");
                        twoWordPolarity = TWEFirst.getTwoWordPolarity();
                        SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());
                        TWESecond.setTwoWord(arrFirst[0] + " " + TWESecond.getTwoWord());
                        TWESecond.getPassedWord().add(TWEFirst.getPassedWord().get(0));
                        TWESecond.setPassedWord(TWESecond.getPassedWord());
                        TWESecond.setTwoWordPolarity(twoWordPolarity);

                        listTwoWords.add(TWESecond);

                    } else {   // bugün çok GÜZEL görünüyor'da i'değişmeden yeni ikili oluşturulacak eski "bugün çok GÜZEL" yeni oluşacak  "çok GÜZEL görünüyor"
                        --i;
                        // isGoto = true;
//                        String[] arrFirst = TWEFirst.getTwoWord().split(" ");
//                        TWESecond.setTwoWord(arrFirst[0] + " " + TWESecond.getTwoWord());
//                        TWESecond.getPassedWord().add(TWEFirst.getPassedWord().get(0));
//                        TWESecond.setPassedWord(TWESecond.getPassedWord());
//                        listTwoWords.add(TWESecond);
//                        twoWordPolarity = TWEFirst.getTwoWordPolarity() + TWESecond.getTwoWordPolarity();
//                        SSE.setSentencePolarity(twoWordPolarity + SSE.getSentencePolarity());
                    }
                } else {
                    listTwoWords.add(TWEFirst);
                    if (Polarity.getPolarityValue(TWEFirst.getPassedWord().get(0).getPolarType()) > 0 && TWEFirst.getPassedWord().get(1).getPolarType() == Polarity.Neutral && TWEFirst.getPassedWord().get(1).getWordCondition() != WordConditionEnum.none) { //İYİ etkilemedi
                        SSE.setSentencePolarity(-1 + SSE.getSentencePolarity());
                    } else if (Polarity.getPolarityValue(TWEFirst.getPassedWord().get(0).getPolarType()) < 0 && TWEFirst.getPassedWord().get(1).getPolarType() == Polarity.Neutral && TWEFirst.getPassedWord().get(1).getWordCondition() != WordConditionEnum.none) { //KÖTÜ etkilemedi
                        SSE.setSentencePolarity(1 + SSE.getSentencePolarity());
                    } else {
                        SSE.setSentencePolarity(TWEFirst.getTwoWordPolarity() + SSE.getSentencePolarity());
                    }
                }
            }
        } else {
            if (listParsedWord.size() > 0) {
                TwoWordsEntity TWE = PTW.decompositionTwoWord(new ArrayList(Arrays.asList(listParsedWord.get(0), null)));
                SSE.setSentencePolarity(TWE.getTwoWordPolarity());
                listTwoWords.add(TWE);
            }
        }

        SSE.setTwoWordEntity(listTwoWords);
        return SSE;
    }
}
