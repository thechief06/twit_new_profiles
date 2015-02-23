/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miya.twit.enums;

import zemberek.core.turkish.PrimaryPos;

/**
 *
 * @author SBT
 */
public enum WordType {

    Adjective, //sıfat
    Adverb, //zarf
    Conjunction, //bağlaç
    Determiner, //belirteç
    Duplicator,
    Interjection, //ünlem
    Noun, //isim
    Numeral, //sayı
    Postpositive, //edat
    Pronoun, //zamir
    Punctuation, //noktalama
    Question, //soru
    Unknown, //bilinmeyen
    Verb, //fiil
    Abbreviation, //Kısaltma
    Complex;

    public static WordType getWordType(PrimaryPos pl) {
        switch (pl) {
            case Adjective:
                return Adjective;
            case Adverb:
                return Adverb;
            case Conjunction:
                return Conjunction;
            case Determiner:
                return Determiner;
            case Duplicator:
                return Duplicator;
            case Interjection:
                return Interjection;
            case Noun:
                return Noun;
            case Numeral:
                return Numeral;
            case PostPositive:
                return Postpositive;
            case Pronoun:
                return Pronoun;
            case Punctuation:
                return Punctuation;
            case Question:
                return Question;
            case Verb:
                return Verb;

            default:
                return Unknown;

        }
    }

    public static int getWordTypeValue(WordType pl) {
        switch (pl) {
            case Adjective:
                return 1;
            case Adverb:
                return 2;
            case Conjunction:
                return 3;
            case Determiner:
                return 4;
            case Duplicator:
                return 5;
            case Interjection:
                return 6;
            case Noun:
                return 7;
            case Numeral:
                return 8;
            case Postpositive:
                return 9;
            case Pronoun:
                return 10;
            case Punctuation:
                return 11;
            case Question:
                return 12;
            case Verb:
                return 13;
            case Unknown:
                return 14;
            case Abbreviation:
                return 15;
            case Complex:
                return 16;

            default:
                return 99;
        }
    }
}
