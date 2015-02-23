/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.pajo;

import com.miya.twit.enums.Polarity;
import java.util.List;

/**
 *
 * @author SBT
 */
public class Expressions {

    public String expression;
    public Polarity polarity;

    public List<String> wordForWord;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public void setPolarity(Polarity polarity) {
        this.polarity = polarity;
    }

    public List<String> getWordForWord() {
        return wordForWord;
    }

    public void setWordForWord(List<String> wordForWord) {
        this.wordForWord = wordForWord;
    }
}
