package com.mrping.face.exception;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public class FaceException extends RuntimeException {

    private String errorCode;
    private String errorMsg;

    public FaceException(String code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

    public FaceException(String code, Throwable cause) {
        super(code, cause);
        errorCode = code;
        errorMsg = cause.getMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
