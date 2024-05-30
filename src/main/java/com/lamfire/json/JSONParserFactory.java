package com.lamfire.json;

import com.lamfire.logger.Logger;
import com.lamfire.utils.ClassUtils;

public class JSONParserFactory {
    private static final Logger LOGGER = Logger.getLogger("JSONParserFactory");
    public static final String FASTJSON2_CLS = "com.alibaba.fastjson2.JSONObject";
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
        JSONParser result = getFastJson2Parser();
        if(result == null){
            result = new JSONThisParser();
        }
        return result;
    }

    private static JSONParser getFastJson2Parser(){
        try {
            Class<?> cls = ClassUtils.getClass(FASTJSON2_CLS);
            if(cls != null){
                return new FastJSON2Parser();
            }
        }catch (Exception e){

        }
        return null;
    }
}
