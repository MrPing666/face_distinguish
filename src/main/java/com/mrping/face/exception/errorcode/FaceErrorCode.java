package com.mrping.face.exception.errorcode;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public enum FaceErrorCode {


    _E1001("E1001","系统异常"),
    _E1002("E1002","参数为空"),
    _E1003("E1003","最大人脸数参数异常"),
    _E1004("E1004","返回参数异常"),
    _E1005("E1005","最大用户数参数异常"),
    _E1006("E1006","用户组ID或用户ID格式错误"),
    _E1007("E1007","参数用户组数量异常"),
    _E1008("E1008", "该用户不存在"),
    _E1009("E1009", "颜值分数异常"),
    _E1010("_E1010", "参数异常"),

    _E2203("22203", "无法解析人脸"),
    _E2229("2229xx", "人脸检测系统繁忙"),
    _E2223("222xxx", "请上传包含清晰人脸的照片"),
    ;

    private String code;
    private String message;

    FaceErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
