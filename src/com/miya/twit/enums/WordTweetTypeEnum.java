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
public enum WordTweetTypeEnum {

    None,
    HashTag,
    Mention,
    Url;

    public static int getWordTweetTypeEnum(WordTweetTypeEnum wtte) {
        switch (wtte) {
            case None:
                return 0;
            case HashTag:
                return 1;
            case Mention:
                return 2;
            case Url:
                return 3;
            default:
                return 99;
        }
    }

    public static WordTweetTypeEnum getWordTweetTypeEnum(int wtte) {
        switch (wtte) {
            case 0:
                return None;
            case 1:
                return HashTag;
            case 2:
                return Mention;
            case 3:
                return Url;
            default:
                return None;
        }
    }
}
