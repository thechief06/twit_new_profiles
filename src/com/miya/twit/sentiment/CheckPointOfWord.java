/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.sentiment;

import com.miya.twit.enums.Polarity;
import com.miya.twit.enums.WordType;
import static com.miya.twit.enums.WordType.getWordTypeValue;
import com.miya.twit.mongodb.DBConnectSentiment;
import com.miya.twit.pajo.ParsedWords;

/**
 *
 * @author SBT
 */
public class CheckPointOfWord {

    public Polarity getPointOfWord(ParsedWords Pword) {
        //şimdilik word typelarında aynı işlem yapılıyor fakat değişecek Dil bilgisine göre
        CheckSpecialConditions CSC = new CheckSpecialConditions();
        if (Pword.getWordType() == WordType.Verb) { //FİİL
            if (Pword.getPolarWord().equals(Pword.getWord())) {
                return CSC.checkTwoWordSpecialConditions(Pword, DBConnectSentiment.getPolarityWithWordType(Pword.getPolarWord(), getWordTypeValue(Pword.getWordType())));
            } else {
                return CSC.checkTwoWordSpecialConditions(Pword, DBConnectSentiment.getPolarityWithWordType(Pword.getPolarWord(), getWordTypeValue(Pword.getWordType())));  //polar word ile word eşit değilse detaylı algoritma üretmek gerek şimdilik boyle dursun
            }
        } else if (Pword.getWordType() == WordType.Noun) { //İSİM
            if (Pword.getPolarWord().equals(Pword.getWord())) {
                Polarity pl = DBConnectSentiment.getPolarityWithWordType(Pword.getWord(), getWordTypeValue(Pword.getWordType()));
                return CSC.checkTwoWordSpecialConditions(Pword, pl);
            } else {
                return CSC.checkTwoWordSpecialConditions(Pword, DBConnectSentiment.getPolarityWithWordType(Pword.getPolarWord(), getWordTypeValue(Pword.getWordType())));  //polar word ile word eşit değilse detaylı algoritma üretmek gerek şimdilik boyle dursun
            }
        } else if (Pword.getWordType() == WordType.Adjective) { //SIFAT
            if (Pword.getPolarWord().equals(Pword.getWord())) {
                return CSC.checkTwoWordSpecialConditions(Pword, DBConnectSentiment.getPolarityWithWordType(Pword.getWord(), getWordTypeValue(Pword.getWordType())));
            } else {
                return CSC.checkTwoWordSpecialConditions(Pword, DBConnectSentiment.getPolarityWithWordType(Pword.getPolarWord(), getWordTypeValue(Pword.getWordType())));  //polar word ile word eşit değilse detaylı algoritma üretmek gerek şimdilik boyle dursun
            }
        } else {
            if (Pword.getPolarWord().equals(Pword.getWord())) {
                return CSC.checkTwoWordSpecialConditions(Pword, DBConnectSentiment.getPolarityWithWordType(Pword.getWord(), getWordTypeValue(Pword.getWordType())));
            } else {
                return CSC.checkTwoWordSpecialConditions(Pword, DBConnectSentiment.getPolarityWithWordType(Pword.getPolarWord(), getWordTypeValue(Pword.getWordType())));  //polar word ile word eşit değilse detaylı algoritma üretmek gerek şimdilik boyle dursun
            }
        }
    }
}
