package com.yhsjedu.datacloud.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class JSON {

	public static JsonMapper nonEmptyMapper = new JsonMapper(Include.NON_EMPTY);
	
	
	public static String decode(Object object) {
		return  nonEmptyMapper.toJson(object);
	}
	

	public static  <T> T encode(String jsonString, Class<T> clazz) {
		return nonEmptyMapper.fromJson(jsonString, clazz);
	}
}
