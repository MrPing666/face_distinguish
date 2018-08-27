package com.mrping.face.dto;

/**
 * @author Created by Mr.Ping on 2018/7/9.
 */
public class UserInfoDTO {

    private String userInfo;
    private String groupId;

    public UserInfoDTO() {}

    public UserInfoDTO(String userInfo, String groupId) {
        this.userInfo = userInfo;
        this.groupId = groupId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public String getGroupId() {
        return groupId;
    }
}
