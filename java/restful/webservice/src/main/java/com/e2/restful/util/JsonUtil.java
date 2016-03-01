package com.e2.restful.util;

import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

    public static Gson getGSON() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();
        return gson;
    }

	public static <T> T fromJson(Class<T> cls, String json) {
		Gson gson = getGSON();
		StringReader reader = new StringReader(json);
    	T obj = gson.fromJson(reader, cls);
    	return obj;
	}
	
	public static String toJson(Object json) {
		Gson gson = getGSON();
		String src = gson.toJson(json);		
    	return src;
	}	
}
