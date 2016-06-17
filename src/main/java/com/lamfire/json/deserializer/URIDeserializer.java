package com.lamfire.json.deserializer;

import java.lang.reflect.Type;
import java.net.URI;

import com.lamfire.json.parser.JavaObjectJSONParser;
import com.lamfire.json.parser.JSONToken;

public class URIDeserializer implements ObjectDeserializer {
    public final static URIDeserializer instance = new URIDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(JavaObjectJSONParser parser, Type clazz) {
        
        String uri = (String) parser.parse();
        
        if (uri == null) {
            return null;
        }
        
        return (T) URI.create(uri);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
