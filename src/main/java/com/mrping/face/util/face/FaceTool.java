package com.mrping.face.util.face;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.FaceVerifyRequest;
import com.baidu.aip.face.MatchRequest;
import com.baidu.aip.util.Base64Util;
import com.mrping.face.dto.FaceInfoDTO;
import com.mrping.face.dto.UserFaceDTO;
import com.mrping.face.dto.UserInfoDTO;
import com.mrping.face.exception.FaceException;
import com.mrping.face.exception.errorcode.FaceErrorCode;
import com.mrping.face.util.StrUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public class FaceTool {

//    public static final String APP_ID = "11477178";
//    public static final String API_KEY = "i5EbKZgouABsNGzHo7Hf0dwz";
//    public static final String SECRET_KEY = "PZlj2ADeSxXha4YjphcL1Fm9skvVGsfw";
    public static final String APP_ID = "11558024";
    public static final String API_KEY = "h8CiWBSeykU7IymVkvX9Kdlo";
    public static final String SECRET_KEY = "Z4Aw7Iqknlf5oX7kbzPgKTbACvmekCyG";
    public static final String GROUP_ID = "star_123";

    static AipFace client = null;
    static {
        client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    }

    /**
     * 人脸检测
     * @param file          图片文件
     * @param max_face_num  最多检测人脸数量
     * @return
     */
    public static String detectFace(File file, String max_face_num) {
        try {
            String imgStr = Base64Util.encode(FileToByte(file));
            return detectFaceScore(imgStr, max_face_num);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸检测得分
     * @param image  图片字节BASE64编码后的字符串
     * @param max_face_num  最多检测人脸数量
     * @return
     */
    public static String detectFaceScore(String image, String max_face_num) {

        HashMap<String, String> options = new HashMap<String, String>(3);
        options.put(FaceConstant.MAX_FACE_NUM, max_face_num);
        options.put(FaceConstant.FACE_TYPE, FaceConstant.TYPE_VALUE);
        options.put(FaceConstant.FACE_FIELD, FaceConstant.FIELD_VALUE);

        JSONObject json = client.detect(image, FaceConstant.IMAGE_TYPE, options);
        jsonDataParser(json);
        JSONArray array = json.getJSONObject(FaceConstant.RESULT).getJSONArray("face_list");
        String beauty = String.valueOf(array.getJSONObject(0).get("beauty"));
        String faceProbability = String.valueOf(array.getJSONObject(0).get("face_probability"));

        if(StrUtil.isBlank(beauty) || StrUtil.isBlank(faceProbability)){
            FaceErrorCode error = FaceErrorCode._E1004;
            throw new FaceException(error.getCode(), error.getMessage());
        }
        if(Double.parseDouble(faceProbability) < FaceConstant.FACE_PROBABILITY){
            FaceErrorCode error = FaceErrorCode._E2203;
            throw new FaceException(error.getCode(), error.getMessage());
        }
        return beauty;
    }

    /**
     * 人脸比对
     * @param fileOne 图片文件
     * @param fileTwo 图片文件
     * @return
     */
    public static FaceInfoDTO matchFace(File fileOne, File fileTwo) {
        try {
            String imageOne = Base64Util.encode(FileToByte(fileOne));
            String imageTwo = Base64Util.encode(FileToByte(fileTwo));
            return matchFace(imageOne, imageTwo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸比对
     * @param imageOne base64编码的图片
     * @param imageTwo base64编码的图片
     * @return
     */
    public static FaceInfoDTO matchFace(String imageOne, String imageTwo) {

        MatchRequest reqOne = new MatchRequest(imageOne, FaceConstant.IMAGE_TYPE);
        MatchRequest reqTwo = new MatchRequest(imageTwo, FaceConstant.IMAGE_TYPE);
        ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
        requests.add(reqOne);requests.add(reqTwo);

        JSONObject json = client.match(requests);
        jsonDataParser(json);
        JSONObject result = json.getJSONObject(FaceConstant.RESULT);
        String score = String.valueOf(result.get(FaceConstant.SCORE));
        if(StrUtil.isBlank(score)){
            FaceErrorCode error = FaceErrorCode._E1004;
            throw new FaceException(error.getCode(), error.getMessage());
        }
        return new FaceInfoDTO(score);
    }

    /**
     * 人脸搜索
     * @param file
     * @param groupIdList
     * @param userId
     * @return
     */
    public static FaceInfoDTO searchFace(File file, String groupIdList, String userId, String maxUserNum) {
        try {
            String image = Base64Util.encode(FileToByte(file));
            return searchFace(image, groupIdList, userId, maxUserNum);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸搜索
     * @param image
     * @param groupIdList
     * @param userId
     * @return
     */
    public static FaceInfoDTO searchFace(String image, String groupIdList, String userId, String maxUserNum) {

        HashMap<String, String> options = new HashMap<String, String>(4);
        if (userId != null) {
            options.put(FaceConstant.USER_ID, userId);
        }
        options.put(FaceConstant.QUALITY, FaceConstant.QUALITY_VALUE);
        options.put(FaceConstant.LIVENESS, FaceConstant.LIVENESS_VALUE);
        options.put(FaceConstant.MAX_USER_NUM, maxUserNum);

        JSONObject json = client.search(image, FaceConstant.IMAGE_TYPE, groupIdList, options);
        jsonDataParser(json);
        JSONObject result = json.getJSONObject(FaceConstant.RESULT);
        JSONArray array = result.getJSONArray("user_list");
        JSONObject obj = array.getJSONObject(0);

        String score = String.valueOf(obj.get(FaceConstant.SCORE));
        String user_id = String.valueOf(obj.get(FaceConstant.USER_ID));
        String userInfo = String.valueOf(obj.get(FaceConstant.USER_INFO));

        Long starId = Long.parseLong(user_id.split("_")[1]);
        return new FaceInfoDTO(score, userInfo, starId);
    }

    /***************************************人脸库管理*****************************************/
    /**
     * 创建用户组
     * @param groupId
     * @return
     */
    public static boolean addGroup(String groupId) {
        JSONObject json = client.groupAdd(groupId, null);
        return jsonDataParser(json);
    }

    /**
     * 删除用户组
     * @param groupId
     * @return
     */
    public static boolean deleteGroup(String groupId) {
        JSONObject json = client.groupDelete(groupId, null);
        return jsonDataParser(json);
    }

    /**
     * 组列表查询
     * @param length
     * @return
     */
    public static List<Object> getGroupList(String length) {
        HashMap<String, String> options = new HashMap<String, String>(2);
        options.put(FaceConstant.START, FaceConstant.START_VALUE);
        if(StrUtil.isNotBlank(length)){
            options.put(FaceConstant.LENGTH, length);
        }

        JSONObject json = client.getGroupList(options);
        jsonDataParser(json);
        JSONObject result = json.getJSONObject(FaceConstant.RESULT);
        JSONArray array = result.getJSONArray("group_id_list");
        return array.toList();
    }

    /**
     * 注册用户人脸
     * @param file
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static boolean addUser(File file, String userInfo, String userId, String groupId) {
        try {
            String image = Base64Util.encode(FileToByte(file));
            return addUser(image, userInfo, userId, groupId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 注册用户人脸
     * @param image
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static boolean addUser(String image, String userInfo, String userId, String groupId) {

        HashMap<String, String> options = new HashMap<String, String>(3);
        options.put(FaceConstant.USER_INFO, userInfo);
        options.put(FaceConstant.QUALITY, FaceConstant.QUALITY_VALUE);
        options.put(FaceConstant.LIVENESS, FaceConstant.LIVENESS_VALUE);

        JSONObject json = client.addUser(image, FaceConstant.IMAGE_TYPE,
                                                groupId, userId, options);
        return jsonDataParser(json);
    }

    /**
     * 注册用户人脸
     * @param image
     * @param userInfo
     * @param userId
     * @return
     */
    public static boolean addUser(String image, String userInfo, String userId) {

        HashMap<String, String> options = new HashMap<String, String>(3);
        options.put(FaceConstant.USER_INFO, userInfo);
        options.put(FaceConstant.QUALITY, FaceConstant.QUALITY_VALUE);
        options.put(FaceConstant.LIVENESS, FaceConstant.LIVENESS_VALUE);

        JSONObject json = client.addUser(image, "URL", GROUP_ID, userId, options);
        return jsonDataParser(json);
    }

    /**
     * 更新用户
     * @param file
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static boolean updateUser(File file, String userInfo, String userId, String groupId) {
        try {
            String image = Base64Util.encode(FileToByte(file));
            return updateUser(image, userInfo, userId, groupId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新用户 人脸更新
     * @param image
     * @param userInfo
     * @param userId
     * @param groupId
     * @return
     */
    public static boolean updateUser(String image, String userInfo, String userId, String groupId) {

        HashMap<String, String> options = new HashMap<String, String>(3);
        if (userInfo != null) {
            options.put(FaceConstant.USER_INFO, userInfo);
        }
        options.put(FaceConstant.QUALITY, FaceConstant.QUALITY_VALUE);
        options.put(FaceConstant.LIVENESS, FaceConstant.LIVENESS_VALUE);

        JSONObject json = client.updateUser(image, FaceConstant.IMAGE_TYPE, groupId, userId, options);
        return jsonDataParser(json);
    }

    /**
     * 删除用户的某一张人脸，如果该用户只有一张人脸图片，则同时删除用户
     * @param userId
     * @param groupId
     * @param faceToken
     * @return
     */
    public static boolean deleteUserFace(String userId, String groupId, String faceToken) {
        JSONObject json = client.faceDelete(userId, groupId, faceToken, null);
        return jsonDataParser(json);
    }

    /**
     * 删除用户组中的用户
     * @param userId
     * @param groupId
     * @return
     */
    public static boolean deleteUser(String userId, String groupId) {
        JSONObject json = client.deleteUser(groupId, userId, null);
        return jsonDataParser(json);
    }

    /**
     * 查询用户信息
     * @param userId
     * @param groupId
     * @return
     */
    public static List<UserInfoDTO> searchUserInfo(String userId, String groupId) {

        JSONObject res = client.getUser(userId, groupId, null);
        jsonDataParser(res);
        JSONObject result = res.getJSONObject(FaceConstant.RESULT);
        JSONArray array = result.getJSONArray("user_list");
        List<UserInfoDTO> list = new ArrayList<UserInfoDTO>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            String userInfo = json.getString(FaceConstant.USER_INFO);
            String id = json.getString(FaceConstant.GROUP_ID);
            list.add(new UserInfoDTO(userInfo, id));
        }
        return list;
    }

    /**
     * 获取用户的人脸列表
     * @param userId
     * @param groupId
     * @return
     */
    public static List<UserFaceDTO> getUserFaceList(String userId, String groupId) {

        JSONObject res = client.faceGetlist(userId, groupId, null);
        jsonDataParser(res);
        JSONObject result = res.getJSONObject(FaceConstant.RESULT);
        JSONArray array = result.getJSONArray("face_list");
        List<UserFaceDTO> list = new ArrayList<UserFaceDTO>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            String faceToken = json.getString(FaceConstant.FACE_TOKEN);
            String createTime = json.getString("ctime");
            list.add(new UserFaceDTO(faceToken, createTime));
        }
        return list;
    }

    /**
     * 获取组中的用户列表
     * @param groupId
     * @param returnNum
     * @return
     */
    public static List<Object> getUserList(String groupId, String returnNum) {
        HashMap<String, String> options = new HashMap<String, String>(2);
        options.put(FaceConstant.START, FaceConstant.START_VALUE);
        if (returnNum != null) {
            options.put(FaceConstant.LENGTH, returnNum);
        }

        JSONObject json = client.getGroupUsers(groupId, options);
        jsonDataParser(json);
        JSONObject result = json.getJSONObject(FaceConstant.RESULT);
        JSONArray array = result.getJSONArray("user_id_list");
        return array.toList();
    }

    /**
     * 复制用户
     * @param userId
     * @param srcGroupId
     * @param dstGroupId
     * @return
     */
    public static boolean userCopy(String userId, String srcGroupId, String dstGroupId) {

        HashMap<String, String> options = new HashMap<String, String>(2);
        options.put(FaceConstant.SRC_GROUP_ID, srcGroupId);
        options.put(FaceConstant.DST_GROUP_ID, dstGroupId);

        JSONObject json = client.userCopy(userId, options);
        return jsonDataParser(json);
    }


    public static boolean jsonDataParser(JSONObject json){

        String code = String.valueOf(json.get(FaceConstant.ERROR_CODE));
        String message = String.valueOf(json.get(FaceConstant.ERROR_MSG));
        if(!FaceConstant.SUCCESS_CODE.equals(code)){
            throw new FaceException(code, message);
        }
        return true;
    }

    /**
     * 检查最大人脸检测数量
     * @param maxFaceNum
     * @return
     */
    public static boolean checkMaxFaceNum(int maxFaceNum){
        if(maxFaceNum < FaceConstant.MIN_FACE ||
                maxFaceNum > FaceConstant.MAX_FACE){
            return true;
        }
        return false;
    }

    /**
     * 检查最大返回用户数量
     * @param maxUserNum
     * @return
     */
    public static boolean checkMaxUserNum(int maxUserNum){
        if(maxUserNum < FaceConstant.MIN_USER ||
                maxUserNum > FaceConstant.MAX_USER){
            return true;
        }
        return false;
    }

    /**
     * 检查最大返回用户组长度
     * @param length
     * @return
     */
    public static boolean checkListLength(int length){
        if(length < 1 || length > FaceConstant.MAX_GROUP_NUM){
            return true;
        }
        return false;
    }

    /**
     * 检查用户ID、用户组ID格式
     * @param id
     * @return
     */
    public static boolean checkForMat(String id){
        String regular = "^(?=.*\\d.*)(?=.*[a-zA-Z].*)(?=.*_.*).*$";
        Pattern compile = Pattern.compile(regular);
        return compile.matcher(id).matches();
    }

    /**
     * 将文件转为流
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] FileToByte(File file) throws IOException {

        InputStream content = new FileInputStream(file);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = content.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

    /**
     * 活体检测
     * @param image
     * @return
     */
    public static String faceverify(byte[] image){
        String imgStr = Base64Util.encode(image);
        FaceVerifyRequest req = new FaceVerifyRequest(imgStr, FaceConstant.IMAGE_TYPE);
        ArrayList<FaceVerifyRequest> list = new ArrayList<FaceVerifyRequest>();
        list.add(req);
        JSONObject res = client.faceverify(list);
        return res.toString();
    }

    /**
     * 身份验证
     * @param image
     * @param idCardNumber
     * @param name
     * @return
     */
    public static String personVerify(byte[] image, String idCardNumber, String name){
        String imgStr = Base64Util.encode(image);
        HashMap<String, String> options = new HashMap<String, String>(2);
        options.put(FaceConstant.QUALITY, FaceConstant.QUALITY_VALUE);
        options.put(FaceConstant.LIVENESS, FaceConstant.LIVENESS_VALUE);

        JSONObject res = client.personVerify(imgStr, FaceConstant.IMAGE_TYPE,
                                                idCardNumber, name, options);
        return  res.toString(2);
    }

}
