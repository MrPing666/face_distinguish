package com.mrping.face.entity;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public enum GameType {

    DETECT_SCORE("测颜值"),
    DETECT_SPOUSE("夫妻相"),
    DETECT_STAR("明星脸"),
    ;

    private String text;

    GameType(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
