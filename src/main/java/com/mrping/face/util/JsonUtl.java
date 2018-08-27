package com.mrping.face.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mrping.face.util.response.DataResponse;

import java.util.*;

/**
 * @author Created by Mr.Ping on 2018/7/9.
 */
public class JsonUtl {

    /**
     * 获取json的head
     * @param json
     * @return
     */
    public static JSONObject getHead(String json) {
        if(StrUtil.isNotBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json).getJSONObject("head");
            return jsonObject;
        }else{
            return null;
        }
    }
    /**
     * 获取json的explain
     * @param json
     * @return
     */
    public static JSONObject getExplain(String json) {
        if(StrUtil.isNotBlank(json)){

            JSONObject jsonObject = JSON.parseObject(json).getJSONObject("explain");
            return jsonObject;
        }else{
            return null;
        }
    }

    /**
     * 获取json参数详情
     * @param json
     * @return
     */
    public static JSONObject getDataDetail(String json) {
        if(StrUtil.isNotBlank(json)){

            JSONObject jsonObject = JSON.parseObject(json).getJSONObject("data");
            return jsonObject;
        }else{
            return null;
        }
    }

    /**
     * 获取data里参数详情的字段
     * @param json 交互的data
     * @param arg 要查参数
     * @return
     */
    public static String getDataDetail(String json,String arg) {
        if(StrUtil.isNotBlank(json)){

            JSONObject jsonObject = JSON.parseObject(json).getJSONObject("data");
            if(jsonObject==null){
                return null;
            }else{
                return jsonObject.getString(arg);
            }
        }else{
            return null;
        }
    }
    /**
     * 获取data里参数详情的字段
     * @param json 交互的data
     * @param arg 要查参数
     * @return
     */
    public static List getDataArray(String json,String arg) {
        if(StrUtil.isNotBlank(json)){

            JSONArray array = JSON.parseObject(json).getJSONObject("data").getJSONArray(arg);
            List<Object> list = new ArrayList<Object>();
            if(array != null)
            {
                for (Object object : array) {
                    list.add(object);
                }
            }
            return list;
        }else{
            return null;
        }
    }

    /**
     * 获取explain里参数详情的字段
     * @param json 交互的data
     * @param arg 要查参数
     * @return
     */
    public static String getExplainDetail(String json,String arg) {
        if(StrUtil.isNotBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json).getJSONObject("explain");
            if(jsonObject==null){
                return null;
            }else{
                return jsonObject.getString(arg);
            }
        }else{
            return null;
        }
    }
    /**
     * 获取head里参数详情的字段
     * @param json 交互的data
     * @param arg 要查参数
     * @return
     */
    public static String getHeadDetail(String json,String arg) {
        if(StrUtil.isNotBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json).getJSONObject("head");
            if(jsonObject==null){
                return null;
            }else{
                return jsonObject.getString(arg);
            }
        }else{
            return null;
        }
    }
    /**
     * 获取当前userId
     * @param json
     * @return
     */
    public static Long getUserId(String json) {
        if(StrUtil.isNotBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json).getJSONObject("explain");
            if(jsonObject==null){
                return null;
            }else{
                Object o = jsonObject.getString("numberID");
                if(o!=null&&StrUtil.isNotBlank(o.toString())){
                    return Long.parseLong(o.toString());
                }else{
                    return null;
                }
            }
        }else{
            return null;
        }

      //  return Long.parseLong(jsonObject.getString("numberID"));
    }

    /**
     *
     * @param errorCode 返回码
     * @param errorMessage  返回码说明
     * @param map  返回参数
     * @return
     */
    public static DataResponse assembleDataResponse(String errorCode, String errorMessage, Map map) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(errorCode);
        dataResponse.setMessage(errorMessage);
        dataResponse.setData(map);
        dataResponse.setResponseTime(System.currentTimeMillis());
        return dataResponse;
    }
    /**
     *
     * @param errorCode 返回码
     * @param errorMessage  返回码说明
     * @param map  返回参数
     * @return
     */
    public static DataResponse assembleDataResponse(Boolean  flag ,String errorCode, String errorMessage,Map map) {
        if(flag){
            map.put("code",errorCode) ;
            map.put("message",errorMessage) ;
         return    assembleDataResponse("SUCCESS","交互成功",map) ;
        }else {
            map.put("code",errorCode) ;
            map.put("message",errorMessage) ;
            return assembleDataResponse("FAILURE","交互失败",map) ;
        }
    }

    /***
     * 将对象转换为HashMap
     * @param str  json格式str
     * @return
     */
    public static HashMap toHashMap(String str)
    {
        if(str==null||str.equals("null"))
        {
            return null;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(str);
        Set it = jsonObject.keySet();
        for(Object o: it)
        {
            String key = String.valueOf(o);
            Object value = jsonObject.get(key);
            map.put(key, value);
        }

        return map;
    }
    public static  void  main(String args[]){
        String data ="{\"data\":{\"password\":\"123asd\"},\"explain\":{\"numberID\":\"16885240\"}}";
        String aa = JsonUtl.getDataDetail(data,"numberID");


    }
    
}
