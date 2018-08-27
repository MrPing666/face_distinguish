package com.mrping.face.util.response;

import java.util.Map;

/**
 * @Description:说明
 * @Author:xgchen
 * @Date:2016-06-30 16:28
 * @Version:V0.0.1
 */
public class DataResponse extends BaseResponse {

    private Map data;

    public DataResponse() {
        super();
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
