package com.lamfire.json.deserializer;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.lamfire.json.parser.JavaObjectJSONParser;
import com.lamfire.json.parser.JSONToken;

public class CharsetDeserializer implements ObjectDeserializer {
    public final static CharsetDeserializer instance = new CharsetDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(JavaObjectJSONParser parser, Type clazz) {
        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        String charset = (String) value;
        
        return (T) Charset.forName(charset);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
