package com.mrping.face.dto;

import com.mrping.face.exception.FaceException;
import com.mrping.face.exception.errorcode.FaceErrorCode;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public enum FaceBeauty {

    LOVE_GUARD("1-4","爱情卫士"),
    EARTH_GUARD("4-7","地球卫士"),
    COSMOS_GUARD("7-10","宇宙卫士"),
    ;

    private String code;
    private String message;

    FaceBeauty(String code, String message){
        this.code = code;
        this.message = message;
    }

    public static FaceBeauty checkYanzhi(String beauty){

        int min = 40;int middle = 70;int max = 100;
        double score = Double.parseDouble(beauty);
        if(score > 0 && score < min){
            return LOVE_GUARD;
        }else if(score > min && score < middle){
            return EARTH_GUARD;
        }else if(score > middle && score <= max){
            return COSMOS_GUARD;
        }else {
            FaceErrorCode error = FaceErrorCode._E1009;
            throw new FaceException(error.getCode(), error.getMessage());
        }
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
