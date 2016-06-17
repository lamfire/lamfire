package com.lamfire.json.deserializer;

import java.lang.reflect.Type;

import com.lamfire.json.parser.JavaObjectJSONParser;
import com.lamfire.json.parser.JSONLexer;
import com.lamfire.json.parser.JSONToken;

public class StringDeserializer implements ObjectDeserializer {

    public final static StringDeserializer instance = new StringDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(JavaObjectJSONParser parser, Type clazz) {
        return (T) deserialze(parser);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T deserialze(JavaObjectJSONParser parser) {
        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String val = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val;
        }
        
        if (lexer.token() == JSONToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return (T) val;
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        return (T) value.toString();
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

}
