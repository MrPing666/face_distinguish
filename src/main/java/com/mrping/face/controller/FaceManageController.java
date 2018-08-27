package com.mrping.face.controller;

import com.alibaba.fastjson.JSON;
import com.mrping.face.dto.UserFaceDTO;
import com.mrping.face.dto.UserInfoDTO;
import com.mrping.face.exception.FaceException;
import com.mrping.face.exception.errorcode.FaceErrorCode;
import com.mrping.face.util.*;
import com.mrping.face.util.face.FaceConstant;
import com.mrping.face.util.face.FaceTool;
import com.mrping.face.util.response.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Mr.Ping on 2018/7/9.
 */
@Controller
@RequestMapping("/faceManage")
public class FaceManageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @RequestMapping(value = "v3/addGroup.mvc", produces = "application/json;charset=UTF-8")
    public String addGroup(String groupId, Map map){

        logger.info("创建用户组,groupId:"+groupId);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(groupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("state", FaceTool.addGroup(groupId));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("创建用户组成功,groupId:"+groupId);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("创建用户组失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("创建用户组失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/deleteGroup.mvc", produces = "application/json;charset=UTF-8")
    public String deleteGroup(String groupId, Map map){

        logger.info("删除用户组,groupId:"+groupId);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(groupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("state", FaceTool.deleteGroup(groupId));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("用户组删除成功,groupId:"+groupId);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户组删除失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户组删除失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/getGroupList.mvc", produces = "application/json;charset=UTF-8")
    public String getGroupList(String length, Map map){

        logger.info("用户组列表查询,length:"+length);
        DataResponse dataResponse;
        try{
            if(StrUtil.isNotBlank(length) &&
                    FaceTool.checkListLength(Integer.parseInt(length))){
                FaceErrorCode error = FaceErrorCode._E1007;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("groupIds", FaceTool.getGroupList(length));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户组列表查询失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户组列表查询失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/addUser.mvc", produces = "application/json;charset=UTF-8")
    public String addUser(String image, String groupId, String userInfo, String faceUserId, Map map){

        logger.info("人脸注册,groupId:"+groupId+",faceUserId:"+faceUserId+",userInfo:"+userInfo);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(image) || StrUtil.isBlank(groupId) || StrUtil.isBlank(faceUserId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(faceUserId) || !FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("state",FaceTool.addUser(image, userInfo, faceUserId, groupId));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("人脸注册成功,groupId:"+groupId+",faceUserId:"+faceUserId+",userInfo:"+userInfo);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("人脸注册失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("人脸注册失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/updateUser.mvc", produces = "application/json;charset=UTF-8")
    public String updateUser(String image, String groupId, String userInfo, String faceUserId, Map map){

        logger.info("人脸更新,groupId:"+groupId+",faceUserId:"+faceUserId+",userInfo:"+userInfo);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(image) || StrUtil.isBlank(faceUserId) || StrUtil.isBlank(groupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(faceUserId) || !FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("state",FaceTool.updateUser(image, userInfo, faceUserId, groupId));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("人脸更新成功,groupId:"+groupId+",faceUserId:"+faceUserId);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("人脸更新失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("人脸更新失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/deleteUserFace.mvc", produces = "application/json;charset=UTF-8")
    public String deleteUserFace(String groupId, String faceToken, String faceUserId, Map map){

        logger.info("人脸删除,groupId:"+groupId+",faceUserId:"+faceUserId+",faceToken:"+faceToken);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(faceUserId) || StrUtil.isBlank(groupId) ||
                                    StrUtil.isBlank(faceToken)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(faceUserId) || !FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("state",FaceTool.deleteUserFace(faceUserId, groupId, faceToken));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("人脸删除成功,groupId:"+groupId+",faceUserId:"+faceUserId+",faceToken:"+faceToken);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("人脸删除失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("人脸删除失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/deleteUser.mvc", produces = "application/json;charset=UTF-8")
    public String deleteUser(String groupId, String faceUserId, Map map){

        logger.info("用户删除,groupId:"+groupId+",faceUserId:"+faceUserId);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(faceUserId) || StrUtil.isBlank(groupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(faceUserId) || !FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("state",FaceTool.deleteUser(faceUserId, groupId));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("用户删除成功,groupId:"+groupId+",faceUserId:"+faceUserId);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户删除失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户删除失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/searchUserInfo.mvc", produces = "application/json;charset=UTF-8")
    public String searchUserInfo(String groupId, String faceUserId, Map map){

        logger.info("用户信息查询,groupId:"+groupId+",faceUserId:"+faceUserId);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(faceUserId) || StrUtil.isBlank(groupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(faceUserId) || !FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            List<UserInfoDTO> userInfoList = FaceTool.searchUserInfo(faceUserId, groupId);
            map.put("userInfoList", userInfoList);
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("用户信息查询成功,groupId:"+groupId+",faceUserId:"+faceUserId);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户信息查询失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户信息查询失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/getUserFaceList.mvc", produces = "application/json;charset=UTF-8")
    public String getUserFaceList(String groupId, String faceUserId, Map map){

        logger.info("用户人脸列表查询,groupId:"+groupId+",faceUserId:"+faceUserId);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(faceUserId) || StrUtil.isBlank(groupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(faceUserId) || !FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            List<UserFaceDTO> userFaceList = FaceTool.getUserFaceList(faceUserId, groupId);
            map.put("userFaceList", userFaceList);
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
            logger.info("用户人脸列表查询成功,groupId:"+groupId+",faceUserId:"+faceUserId);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户人脸列表查询失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户人脸列表查询失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/getUserList.mvc", produces = "application/json;charset=UTF-8")
    public String getUserList(String groupId, String length, Map map){

        logger.info("用户列表查询,groupId:"+groupId+",length:"+length);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(groupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(groupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(StrUtil.isBlank(length)){
                length = FaceConstant.LENGTH_VALUE;
            }
            if(FaceTool.checkListLength(Integer.parseInt(length))){
                FaceErrorCode error = FaceErrorCode._E1007;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("userIds", FaceTool.getUserList(groupId, length));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户列表查询失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户列表查询失败,Exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

    @ResponseBody
    @RequestMapping(value = "v1/userCopy.mvc", produces = "application/json;charset=UTF-8")
    public String userCopy(String faceUserId, String srcGroupId, String dstGroupId, Map map){

        logger.info("用户复制,人脸库用户ID:"+faceUserId+",复制组:"+srcGroupId+",添加组:"+dstGroupId);
        DataResponse dataResponse;
        try{
            if(StrUtil.isBlank(faceUserId) || StrUtil.isBlank(srcGroupId) ||
                    StrUtil.isBlank(dstGroupId)){
                FaceErrorCode error = FaceErrorCode._E1002;
                throw new FaceException(error.getCode(), error.getMessage());
            }
            if(!FaceTool.checkForMat(faceUserId) || !FaceTool.checkForMat(srcGroupId) ||
                    !FaceTool.checkForMat(dstGroupId)){
                FaceErrorCode error = FaceErrorCode._E1006;
                throw new FaceException(error.getCode(), error.getMessage());
            }

            map.put("state", FaceTool.userCopy(faceUserId, srcGroupId, dstGroupId));
            dataResponse= JsonUtl.assembleDataResponse("SUCCESS","交互成功",map);
        }catch (FaceException e){
            map.put("code", e.getErrorCode());
            map.put("message", e.getErrorMsg());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户复制失败,FaceException:"+e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", FaceErrorCode._E1001.getCode());
            map.put("message", FaceErrorCode._E1001.getMessage());
            dataResponse= JsonUtl.assembleDataResponse("FAILURE","操作失败",map);
            logger.error("用户复制失败,exception:"+e.getMessage());
        }
        return JSON.toJSONString(dataResponse);
    }

}
