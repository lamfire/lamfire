package com.lamfire.json;

import com.alibaba.fastjson2.JSONObject;
import com.lamfire.utils.ClassUtils;

import java.util.Collection;
import java.util.Collections;

class FastJSON2Parser implements JSONParser{

    @Override
    public String toJSONString(Object obj) {
        if(ClassUtils.isCollectionType(obj.getClass())){
            com.alibaba.fastjson2.JSONArray arr = com.alibaba.fastjson2.JSONArray.from(obj);
            return arr.toJSONString();
        }
        return JSONObject.from(obj).toJSONString();
    }

    @Override
    public <T> T toJavaObject(String json, Class<T> clazz) {
        return JSONObject.parseObject(json,clazz);
    }

    @Override
    public JSON parse(String json) {
        JSONObject jo = JSONObject.parseObject(json);
        return new JSON(jo);
    }

    @Override
    public JSONArray parseArray(String json) {
        com.alibaba.fastjson2.JSONArray arr = com.alibaba.fastjson2.JSONArray.parse(json);
        return new JSONArray(arr);
    }
}
