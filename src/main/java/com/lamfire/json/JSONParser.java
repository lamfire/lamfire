package com.lamfire.json;

public interface JSONParser {
    String toJSONString(Object obj);

    <T> T toJavaObject(String json,Class<T> clazz);

    JSON parse(String json);

    JSONArray parseArray(String json);
}
