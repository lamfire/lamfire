package com.lamfire.json.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import com.lamfire.json.JSONException;
import com.lamfire.json.parser.DefaultExtJSONParser;
import com.lamfire.json.parser.JSONToken;

@SuppressWarnings({ "unchecked"})
public class CollectionDeserializer implements ObjectDeserializer {

    public final static CollectionDeserializer instance = new CollectionDeserializer();

 
    public <T> T deserialze(DefaultExtJSONParser parser, Type type) {
        if (parser.getLexer().token() == JSONToken.NULL) {
            parser.getLexer().nextToken(JSONToken.COMMA);
            return null;
        }

        //to class
        Class<?> clazz = null;
        if (type instanceof Class<?>) {
           clazz = (Class<?>) type;
        }else if(type instanceof ParameterizedType){
            ParameterizedType paramType = (ParameterizedType)type;
            clazz = (Class<?>)paramType.getRawType();
        }

        //instance collection
        Collection list = null;
        if(clazz != null){
            if (clazz.isAssignableFrom(Collection.class)) {
                list = new ArrayList();
            }else if (clazz.isAssignableFrom(ArrayList.class)) {
                list = new ArrayList();
            }else if (clazz.isAssignableFrom(LinkedList.class)) {
                list = new LinkedList();
            }else if (clazz.isAssignableFrom(List.class)) {
                list = new ArrayList();
            } else if (clazz.isAssignableFrom(HashSet.class)) {
                list = new HashSet();
            }else if (clazz.isAssignableFrom(TreeSet.class)) {
                list = new TreeSet();
            }  else if (clazz.isAssignableFrom(Set.class)) {
                list = new HashSet();
            } else {
                try {
                    list = (Collection) clazz.newInstance();
                } catch (Exception e) {
                    throw new JSONException("create instane error, class " + clazz.getName());
                }
            }
        }

        if (list == null) {
            list = new ArrayList();
        }

        //element type
        Type itemType;
        if (type instanceof ParameterizedType) {
            itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            itemType = Object.class;
        }
        parser.parseArray(itemType, list);

        return (T) list;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}
