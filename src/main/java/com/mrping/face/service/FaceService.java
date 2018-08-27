package com.mrping.face.service;

import com.mrping.face.dto.FaceInfoDTO;
import com.mrping.face.entity.GameRecord;
import com.mrping.face.entity.GameType;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public interface FaceService {

    /**
     * 仓储
     * @param gameRecord
     */
    void put(GameRecord gameRecord);

    /**
     * 获取颜值游戏测试总数
     * @param gameType 颜值游戏类型
     * @return
     */
    Long getGameSum(GameType gameType);

    /**
     * 测颜值
     * @param userId     用户ID
     * @param image      图片Base64编码后的字符
     * @param maxFaceNum 最多检测的人脸数量
     * @return
     */
    FaceInfoDTO detectFaceScore(Long userId, String image, String maxFaceNum);

    /**
     * 测夫妻相
     * @param userId   用户ID
     * @param imageOne 图片Base64编码后的字符
     * @param imageTwo 图片Base64编码后的字符
     * @return
     */
    FaceInfoDTO detectSpouseFace(Long userId, String imageOne, String imageTwo);


    /**
     * 测明星相
     * @param userId     用户ID
     * @param image      图片Base64编码后的字符
     * @param groupIds   人脸库组ID字符串连接
     * @param maxUserNum 最大返回明星数量
     * @return
     */
    FaceInfoDTO detectStarFace(Long userId, String image, String groupIds, String maxUserNum);

}
