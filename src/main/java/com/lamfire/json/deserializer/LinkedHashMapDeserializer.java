package com.lamfire.json.deserializer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.lamfire.json.parser.JavaObjectJSONParser;
import com.lamfire.json.parser.JSONToken;

public class LinkedHashMapDeserializer implements ObjectDeserializer {
    public final static LinkedHashMapDeserializer instance = new LinkedHashMapDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(JavaObjectJSONParser parser, Type clazz) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        parser.parseObject(map);
        return (T) map;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
