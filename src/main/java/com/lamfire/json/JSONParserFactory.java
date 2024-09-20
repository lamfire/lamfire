package com.lamfire.json;

import com.lamfire.logger.Logger;
import com.lamfire.utils.ClassUtils;

public class JSONParserFactory {
    private static final Logger LOGGER = Logger.getLogger("JSONParserFactory");
    private static JSONParser parser;

    public static  JSONParser getJSONParser(){
        if(parser != null){
            return parser;
        }
        parser = scanOther();
        LOGGER.info("[JSONParser] : " + parser.getClass().getName());
        return parser;
    }

    private static JSONParser scanOther(){
        return new JSONThisParser();
    }

}
