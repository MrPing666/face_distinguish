package com.mrping.face.dto;

/**
 * @author Created by Mr.Ping on 2018/7/9.
 */
public class UserFaceDTO {

    private String faceToken;
    private String createTime;

    public UserFaceDTO() {}

    public UserFaceDTO(String faceToken, String createTime) {
        this.faceToken = faceToken;
        this.createTime = createTime;
    }

    public String getFaceToken() {
        return faceToken;
    }

    public String getCreateTime() {
        return createTime;
    }
}
