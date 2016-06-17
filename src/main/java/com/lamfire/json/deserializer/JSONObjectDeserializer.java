package com.lamfire.json.deserializer;

import java.lang.reflect.Type;

import com.lamfire.json.parser.JavaObjectJSONParser;
import com.lamfire.json.parser.JSONToken;

public class JSONObjectDeserializer implements ObjectDeserializer {
    public final static JSONObjectDeserializer instance = new JSONObjectDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(JavaObjectJSONParser parser, Type clazz) {
        return (T) parser.parseObject();
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
