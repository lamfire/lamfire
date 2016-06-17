package com.lamfire.json.deserializer;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import com.lamfire.json.JSONException;
import com.lamfire.json.parser.JavaObjectJSONParser;
import com.lamfire.json.parser.JSONToken;

public class URLDeserializer implements ObjectDeserializer {
    public final static URLDeserializer instance = new URLDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(JavaObjectJSONParser parser, Type clazz) {
        
        String url = (String) parser.parse();
        
        if (url == null) {
            return null;
        }
        
        try {
            return (T) new URL(url);
        } catch (MalformedURLException e) {
            throw new JSONException("create url error", e);
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
