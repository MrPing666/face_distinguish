package com.mrping.face.util.response;


import com.mrping.face.exception.errorcode.ErrorCode;

/**
 * @Description:说明
 * @Author:xgchen
 * @Date:2016-06-30 16:27
 * @Version:V0.0.1
 */
public class BaseResponse {

    private String code = ErrorCode.SUCCESS.getLabel();

    private String message;

    private Long responseTime;

    public BaseResponse() {
    }

    public BaseResponse(String code, String message, Long responseTime) {
        this.code = code;
        this.message = message;
        this.responseTime = responseTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }
}
