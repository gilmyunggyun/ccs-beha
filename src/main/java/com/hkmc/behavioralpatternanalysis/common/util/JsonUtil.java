package com.hkmc.behavioralpatternanalysis.common.util;


import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

@SuppressWarnings("unchecked")
public class JsonUtil {
	
	private static JsonFactory factory = null;
	private static ObjectMapper mapper = null;
	private static ObjectWriter writer = null;
	private static ObjectReader reader = null;
	
	public static void init() throws RuntimeException{
		factory = new JsonFactory();
		mapper = new ObjectMapper();
		mapper.registerModule(new AfterburnerModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		writer = mapper.writer();
		reader = mapper.reader();
	}
			
	public static Map<String, Object> str2map(String in) {
		try {
			if( mapper == null) init();
			if (in == null) return null;
			
			TypeReference<Map<String,Object>> typeRef 
	        	= new TypeReference<Map<String,Object>>() {};
			
			return mapper.readValue(in.intern(), typeRef);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, String> toLowerCaseMap(Map<String, String> inMap) {
		if(inMap == null) return null;
		Map<String, String> outMap = new HashMap<String, String>();
		for(Map.Entry<String, String> entry : inMap.entrySet()) {
			outMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		return outMap;
	}
}
