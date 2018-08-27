package com.mrping.face.controller;

import com.alibaba.fastjson.JSON;
import com.mrping.face.dto.FaceBeauty;
import com.mrping.face.dto.FaceInfoDTO;
import com.mrping.face.entity.GameRecord;
import com.mrping.face.entity.GameType;
import com.mrping.face.exception.FaceException;
import com.mrping.face.exception.errorcode.FaceErrorCode;
import com.mrping.face.repository.GameRecordRepository;
import com.mrping.face.service.FaceService;
import com.mrping.face.util.*;
import com.mrping.face.util.face.FaceConstant;
import com.mrping.face.util.face.FaceTool;
import com.mrping.face.util.response.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
@Controller
@RequestMapping("/face")
public class FaceController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private FaceService faceService;

    private final Long userId = 110112L;

    @ResponseBody
    @RequestMapping(value = "v1/detectFaceScore.mvc", produces = "application/json;charset=UTF-8")
    public String detectFaceScore(String image, String maxFaceNum, HttpServletRequest request, Map map) {

        DataResponse dataResponse;
        try {
            if (StrUtil.isBlank(image)) {
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if (StrUtil.isBlank(maxFaceNum)) {
                maxFaceNum = String.valueOf(FaceConstant.MIN_FACE);
            }
            if (FaceTool.checkMaxFaceNum(Integer.parseInt(maxFaceNum))) {
                FaceErrorCode error = FaceErrorCode._E1003;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            FaceInfoDTO faceInfoDTO = faceService.detectFaceScore(userId, image, maxFaceNum);
            FaceBeauty beauty = faceInfoDTO.getBeauty();

            if (null != beauty) {
                StringBuffer url = request.getRequestURL();
                String domainName = url.toString().substring(0, url.toString().indexOf("/face"));

                String positionPath = domainName + "/img/face_score/position.png";
                String backImagePath = domainName + "/img/face_score/face_score.png";

                waterMarkFaceScore(faceInfoDTO, image, backImagePath, positionPath);

                GameRecord gameRecord = new GameRecord(userId, faceInfoDTO.getScore(), faceInfoDTO.getImgUrl(),
                        faceInfoDTO.getShareImgUrl(), GameType.DETECT_SCORE);
                faceService.put(gameRecord);

                faceInfoDTO.saveGameId(gameRecord.getId());
            }

            map.put("faceInfoDTO", faceInfoDTO);
            dataResponse = JsonUtl.assembleDataResponse("SUCCESS", "交互成功", map);

        } catch (FaceException e) {
            String code = e.getErrorCode();
            String message = e.getErrorMsg();
            if (code.contains(FaceConstant.FACE_ERROR_CODE_PREFIX)) {
                code = FaceErrorCode._E2229.getCode();
                message = FaceErrorCode._E2229.getMessage();
            }
            if(code.contains(FaceConstant.FACE_ERROR_PREFIX)) {
                code = FaceErrorCode._E2223.getCode();
                message = FaceErrorCode._E2223.getMessage();
            }
            map.put("code", code);map.put("message", message);
            logger.error("颜值检测异常," + code + "-" + message);
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            logger.error("颜值检测异常," + e.getMessage());
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/detectSpouseFace.mvc", produces = "application/json;charset=UTF-8")
    public String spouseFace(String imageLeft, String imageRight, HttpServletRequest request, Map map) {

        DataResponse dataResponse;
        try {
            if (StrUtil.isBlank(imageLeft) || StrUtil.isBlank(imageRight)) {
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            StringBuffer url = request.getRequestURL();
            String ip = url.toString().substring(0, url.toString().indexOf("/face"));

            FaceInfoDTO faceInfoDTO = faceService.detectSpouseFace(userId, imageLeft, imageRight);

            waterMarkSpouse(faceInfoDTO, imageLeft, imageRight, ip);

            GameRecord gameRecord = new GameRecord(userId, faceInfoDTO.getScore(), faceInfoDTO.getImgUrl(),
                    faceInfoDTO.getShareImgUrl(), GameType.DETECT_SPOUSE);
            faceService.put(gameRecord);

            faceInfoDTO.saveGameId(gameRecord.getId());

            map.put("faceInfoDTO", faceInfoDTO);
            dataResponse = JsonUtl.assembleDataResponse("SUCCESS", "交互成功", map);

        } catch (FaceException e) {
            String code = e.getErrorCode();
            String message = e.getErrorMsg();
            if (code.contains(FaceConstant.FACE_ERROR_CODE_PREFIX)) {
                code = FaceErrorCode._E2229.getCode();
                message = FaceErrorCode._E2229.getMessage();
            }
            if(code.contains(FaceConstant.FACE_ERROR_PREFIX)) {
                code = FaceErrorCode._E2223.getCode();
                message = FaceErrorCode._E2223.getMessage();
            }
            map.put("code", code);map.put("message", message);
            logger.error("测夫妻相异常," + code + "-" + message);
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            logger.error("测夫妻相异常,Exception:" + e.getMessage());
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/detectStarFace.mvc", produces = "application/json;charset=UTF-8")
    public String detectStarFace(String image, String maxUserNum, HttpServletRequest request, Map map) {

        DataResponse dataResponse;
        try {
            if (userId == null || StrUtil.isBlank(image)) {
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if (StrUtil.isBlank(maxUserNum)) {
                maxUserNum = String.valueOf(FaceConstant.MIN_FACE);
            }
            if (FaceTool.checkMaxUserNum(Integer.parseInt(maxUserNum))) {
                FaceErrorCode error = FaceErrorCode._E1005;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            StringBuffer url = request.getRequestURL();
            String ip = url.toString().substring(0, url.toString().indexOf("/face"));

            FaceInfoDTO faceInfoDTO = faceService.detectStarFace(userId, image, FaceTool.GROUP_ID, maxUserNum);

            waterMarkStar(faceInfoDTO, image, ip);

            GameRecord gameRecord = new GameRecord(userId, faceInfoDTO.getScore(), faceInfoDTO.getImgUrl(),
                                                    faceInfoDTO.getShareImgUrl(), GameType.DETECT_STAR);
            faceService.put(gameRecord);

            faceInfoDTO.saveGameId(gameRecord.getId());

            map.put("faceInfoDTO", faceInfoDTO);
            logger.info("测明星相成功,imageUrl:" + faceInfoDTO.getImageUrl());
            dataResponse = JsonUtl.assembleDataResponse("SUCCESS", "交互成功", map);

        } catch (FaceException e) {
            String code = e.getErrorCode();
            String message = e.getErrorMsg();
            if (code.contains(FaceConstant.FACE_ERROR_CODE_PREFIX)) {
                code = FaceErrorCode._E2229.getCode();
                message = FaceErrorCode._E2229.getMessage();
            }
            if(code.contains(FaceConstant.FACE_ERROR_PREFIX)) {
                code = FaceErrorCode._E2223.getCode();
                message = FaceErrorCode._E2223.getMessage();
            }
            map.put("code", code);map.put("message", message);
            logger.error("测明星相异常," + code + "-" + message);
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            logger.error("测明星相异常,Exception:" + e.getMessage());
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/detectFace.mvc", produces = "application/json;charset=UTF-8")
    public String faceDetect(String image, String maxFaceNum, Map map) {

        DataResponse dataResponse;
        try {
            if (StrUtil.isBlank(image)) {
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if (StrUtil.isBlank(maxFaceNum)) {
                maxFaceNum = String.valueOf(FaceConstant.MIN_FACE);
            }
            if (FaceTool.checkMaxFaceNum(Integer.parseInt(maxFaceNum))) {
                FaceErrorCode error = FaceErrorCode._E1003;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("score", FaceTool.detectFaceScore(image, maxFaceNum));
            dataResponse = JsonUtl.assembleDataResponse("SUCCESS", "交互成功", map);

        } catch (FaceException e) {
            String code = e.getErrorCode();
            String message = e.getErrorMsg();
            if (code.contains(FaceConstant.FACE_ERROR_CODE_PREFIX)) {
                code = FaceErrorCode._E2229.getCode();
                message = FaceErrorCode._E2229.getMessage();
            }
            if(code.contains(FaceConstant.FACE_ERROR_PREFIX)) {
                code = FaceErrorCode._E2223.getCode();
                message = FaceErrorCode._E2223.getMessage();
            }
            map.put("code", code);map.put("message", message);
            logger.error("人脸检测异常," + code + "-" + message);
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            logger.error("人脸检测异常," + e.getMessage());
            dataResponse = JsonUtl.assembleDataResponse("FAILURE", "操作失败", map);
        }
        return JSON.toJSONString(dataResponse);
    }
    

    public void waterMarkFaceScore(FaceInfoDTO faceInfoDTO, String image, String backImagePath, String positionPath) {
        try {
            if (StrUtil.isNotBlank(image)) {

                String sourcePath = ImageUtil.generateImage(image, "/web/temp/face_score_source.png");
                String outIconPath = "/web/temp/face_test_position.png";
                String outImgPath = "/web/temp/face_score_result.png";
                String outSharePath = "/web/temp/face_score_share.png";

                ImageUtil.zoomInImage(sourcePath, 300, 300);
                ImageUtil.shear(sourcePath, positionPath, outIconPath, Color.YELLOW, 5.0f);

                FaceBeauty faceBeauty = faceInfoDTO.getBeauty();
                if (faceBeauty == FaceBeauty.LOVE_GUARD) {
                    WaterMarkUtil.waterMarkOptionsIcon(1, 597, 824, 150, 150, true, 0, 0);
                } else if ((faceBeauty == FaceBeauty.EARTH_GUARD)) {
                    WaterMarkUtil.waterMarkOptionsIcon(1, 704, 501, 150, 150, true, 0, 0);
                } else if ((faceBeauty == FaceBeauty.COSMOS_GUARD)) {
                    WaterMarkUtil.waterMarkOptionsIcon(1, 1095, 284, 150, 150, true, 0, 0);
                }

                WaterMarkUtil.waterMarkByImg(outIconPath, backImagePath, outImgPath, null, null, null);
                ImageUtil.cropPngImage(outImgPath, outSharePath, 0, 0, 1445, 1321);

            }
        } catch (Exception e) {
            logger.error("测试颜值处理分享图片异常。信息" + e);
        }
    }

    /**
     * 测夫妻相添加水印
     * @param faceInfoDTO 人脸对比返回数据
     * @param imageLeft   左边的图片（base64）
     * @param imageRight  右边的图片（base64）
     * @param ip          ip
     * @return
     * @throws IOException
     */
    public void waterMarkSpouse(FaceInfoDTO faceInfoDTO, String imageLeft, String imageRight, String ip) throws IOException {

        String sourcePath = ip + "/img/face_spouse/spouse_share.png";
        String urlSuffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);

        String outImgPath = "/web/temp/spouse." + urlSuffix;
        String outSharePath = "/web/temp/spouse_share." + urlSuffix;

        String leftPath = ImageUtil.generateImage(imageLeft, "/web/temp/source_left.png");
        String rightPath = ImageUtil.generateImage(imageRight, "/web/temp/source_right.png");

        Watermark.shearStroke(leftPath, leftPath, new Color(255, 122, 151), 10, 476);
        Watermark.shearStroke(rightPath, rightPath, new Color(255, 122, 151), 10, 476);

        Watermark.waterMarkImgAlign(703, 280);
        Watermark.waterMarkByImg(rightPath, sourcePath, outImgPath);

        Watermark.waterMarkImgAlign(287, 280);
        Watermark.waterMarkByImg(leftPath, outImgPath, outImgPath);

        String score = faceInfoDTO.getScore() + "分";
        Watermark.waterMarkTextAlign(913, 937, new Color(255, 122, 151), new Font("微软雅黑", Font.PLAIN, 60));
        Watermark.waterMarkByText(score, outImgPath, outImgPath);

        String ratio = faceInfoDTO.getExceedRatio();
        String text = "超过了全国" + ratio + "的情侣";
        Watermark.spouseRatioTextAlign(ratio, new Color(102, 102, 102), new Font("微软雅黑", Font.PLAIN, 52));
        Watermark.waterMarkByText(text, outImgPath, outImgPath);

        ImageUtil.cropPngImage(outImgPath, outSharePath, 0, 0, 1446, 1227);

    }

    /**
     * 测明星相添加水印
     * @param faceInfoDTO  人脸搜索返回数据
     * @param image        用户上传人脸照（base64）
     * @param ip           ip
     * @throws IOException
     */
    public void waterMarkStar(FaceInfoDTO faceInfoDTO, String image, String ip) throws IOException {

        String arcImgPath = ip + "/img/face_star/arc.png";
        String sourcePath = ip + "/img/face_star/star" + faceInfoDTO.getStarIndex() + ".png";

        String urlSuffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);

        String outImgPath = "/web/temp/star." + urlSuffix;
        String outSharePath = "/web/temp/star_share." + urlSuffix;

        String leftPath = ImageUtil.generateImage(image, "/web/temp/star_left.png");
        String rightPath = "/web/temp/star_right.png";

        String starImgUrl = faceInfoDTO.getImageUrl();
        Watermark.shearStroke(leftPath, leftPath, new Color(37,121,214), 0, 121);
        Watermark.shearStroke(starImgUrl, rightPath, new Color(37,121,214), 0, 121);

        Watermark.waterMarkImgAlign(180,70);
        Watermark.waterMarkByImg(rightPath, sourcePath, outImgPath);

        Watermark.waterMarkImgAlign(180,70);
        Watermark.waterMarkByImg(arcImgPath, outImgPath, outImgPath);

        Watermark.waterMarkImgAlign(80,70);
        Watermark.waterMarkByImg(leftPath, outImgPath, outImgPath);

        Watermark.waterMarkImgAlign(80,70);
        Watermark.waterMarkByImg(arcImgPath, outImgPath, outImgPath);

        Integer score = faceInfoDTO.getScore();
        Watermark.starScoreTextAlign(score + "%", new Color(4,162,241), new Font("微软雅黑", Font.PLAIN, 12));
        Watermark.waterMarkByText("相似度" + score + "%", outImgPath, outImgPath);

        Watermark.waterMarkTextAlign((375 - Watermark.progressBarW)/2, 50, null, null);
        Watermark.paintProgressBar(outImgPath, outImgPath, score/100d);

        String starName = faceInfoDTO.getUserInfo();
        Watermark.waterMarkTextAlign(93, 270, new Color(51,51,51), new Font("微软雅黑", Font.PLAIN, 15));
        Watermark.waterMarkByText("最像的明星: " + starName, outImgPath, outImgPath);

        ImageUtil.cropPngImage(outImgPath, outSharePath, 0, 0, 375, 318);

//        String urlOne = "/" + ParamUtil.getUUID() + "." + urlSuffix;
//        File tempFile = new File(outSharePath);
//        UpYunUtils.writeFile("/face/star" + urlOne, tempFile);
//
//        String urlTwo = "/" + ParamUtil.getUUID() + "." + urlSuffix;
//        File tempShareFile = new File(outImgPath);
//        UpYunUtils.writeFile("/face/star_share" + urlTwo, tempShareFile);
//
//        if (!FileUtils.deleteQuietly(tempFile)) {
//            logger.info("删除水印图片失败");
//        }
//        if (!FileUtils.deleteQuietly(new File(leftPath))) {
//            logger.info("删除水印源图片失败");
//        }
//        if (!FileUtils.deleteQuietly(new File(outImgPath))) {
//            logger.info("删除水印ICON失败");
//        }
//        if (!FileUtils.deleteQuietly(new File(outSharePath))) {
//            logger.info("删除分享图片失败");
//        }
//        String waterMarkImgUrl = IMAGEPATH_PREFIX + "/face/star" + urlOne;
//        String shareImgUrl = IMAGEPATH_PREFIX + "/face/star_share" + urlTwo;
//        faceInfoDTO.saveImgUrl(waterMarkImgUrl, shareImgUrl);
    }


}
