package com.mrping.face.service;

import com.mrping.face.dto.FaceInfoDTO;
import com.mrping.face.entity.GameRecord;
import com.mrping.face.entity.GameType;
import com.mrping.face.exception.FaceException;
import com.mrping.face.exception.errorcode.FaceErrorCode;
import com.mrping.face.repository.GameRecordRepository;
import com.mrping.face.util.StrUtil;
import com.mrping.face.util.face.FaceTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
@Service
@Transactional
public class FaceServiceImpl implements FaceService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private GameRecordRepository gameRecordRepository;

    @Override
    public void put(GameRecord gameRecord) {
        gameRecordRepository.save(gameRecord);
    }

    @Override
    public Long getGameSum(GameType gameType) {
        return gameRecordRepository.getCountByGameType(gameType);
    }

    @Override
    public FaceInfoDTO detectFaceScore(Long userId, String image, String maxFaceNum) {
        String beauty = FaceTool.detectFaceScore(image, maxFaceNum);
        int score = Integer.parseInt(beauty.split("\\.")[0]);
        return new FaceInfoDTO(score, beauty);
    }

    @Override
    public FaceInfoDTO detectSpouseFace(Long userId, String imageOne, String imageTwo) {
        FaceInfoDTO faceInfoDTO = FaceTool.matchFace(imageOne, imageTwo);
        return faceInfoDTO;
    }

    @Override
    public FaceInfoDTO detectStarFace(Long userId, String image, String groupIds, String maxUserNum) {
        FaceInfoDTO faceInfoDTO = FaceTool.searchFace(image, groupIds, null, maxUserNum);
        return faceInfoDTO;
    }

}
