package com.lamfire.json.deserializer;

import java.lang.reflect.Type;

import com.lamfire.json.parser.JavaObjectJSONParser;

public interface ObjectDeserializer {
    <T> T deserialze(JavaObjectJSONParser parser, Type type);
    
    int getFastMatchToken();
}
