package com.mrping.face.util.face;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public class FaceConstant {

    public static final String IMAGE_TYPE = "BASE64";

    /******************************detect********************************/
    /**默认只返回face_token、人脸框、概率和旋转角度**/
    public static final String FACE_FIELD = "face_field";
    public static final String FIELD_VALUE = "age,beauty,gender";
    /**male:男性 female:女性**/
    public static final String FIELD_SEX = "male";

    /**最多处理人脸的数目，默认值为1**/
    public static final String MAX_FACE_NUM = "max_face_num";
    public static final int MIN_FACE = 1;
    public static final int MAX_FACE = 10;

    /**人脸的类型,LIVE表示生活照**/
    public static final String FACE_TYPE = "face_type";
    public static final String TYPE_VALUE = "LIVE";

    public static final Double FACE_PROBABILITY = 0.7;

    /******************************search********************************/
    /**图片质量控制,默认NONE: 不控制,LOW:较低的质量要求
           NORMAL: 一般的质量要求,HIGH: 较高的质量要求**/
    public static final String QUALITY = "quality_control";
    public static final String QUALITY_VALUE = "NONE";

    /**活体检测控制,默认NONE: 不控制,LOW:较低的
                   NORMAL: 一般的,HIGH: 较高的**/
    public static final String LIVENESS = "liveness_control";
    public static final String LIVENESS_VALUE = "NONE";

    /**查找后返回的用户数量,默认为1**/
    public static final String MAX_USER_NUM = "max_user_num";
    public static final int MIN_USER = 1;
    public static final int MAX_USER = 20;

    public static final String GROUP_ID = "group_id";
    public static final String USER_ID = "user_id";
    public static final String USER_INFO = "user_info";

    /******************************getGroupUsers********************************/
    /**默认值0，起始序号**/
    public static final String START = "start";
    public static final String START_VALUE = "0";
    /**返回数量，默认值100**/
    public static final String LENGTH = "length";
    public static final String LENGTH_VALUE = "100";

    /******************************userCopy********************************/
    /**用户所在组ID**/
    public static final String SRC_GROUP_ID = "src_group_id";
    /**需要添加用户的组id**/
    public static final String DST_GROUP_ID = "dst_group_id";

    /**最大返回用户组数量**/
    public static final int MAX_GROUP_NUM = 1000;
    /**返回的测试分数字段**/
    public static final String SCORE = "score";
    /**返回的json对象**/
    public static final String RESULT = "result";
    /**返回成功码**/
    public static final String SUCCESS_CODE = "0";
    /**返回成功码说明**/
    public static final String SUCCESS = "SUCCESS";
    /**人脸标识字段**/
    public static final String FACE_TOKEN = "face_token";
    /**返回的错误码**/
    public static final String ERROR_CODE = "error_code";
    /**返回的错误说明**/
    public static final String ERROR_MSG = "error_msg";

    public static final String FACE_ERROR_PREFIX = "2223";

    public static final String FACE_ERROR_CODE_PREFIX = "2229";

}
