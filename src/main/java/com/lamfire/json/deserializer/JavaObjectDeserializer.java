package com.lamfire.json.deserializer;

import java.lang.reflect.Type;

import com.lamfire.json.parser.JavaObjectJSONParser;
import com.lamfire.json.parser.JSONToken;

public class JavaObjectDeserializer implements ObjectDeserializer {

    public final static JavaObjectDeserializer instance = new JavaObjectDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(JavaObjectJSONParser parser, Type clazz) {
        return (T) parser.parse();
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
