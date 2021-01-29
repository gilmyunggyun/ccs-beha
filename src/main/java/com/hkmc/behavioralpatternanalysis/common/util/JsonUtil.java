package com.hkmc.behavioralpatternanalysis.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonUtil {

	private static ObjectMapper mapper = null;
	private static ObjectWriter writer = null;

	public static void init() throws RuntimeException {
		mapper = new ObjectMapper();
		mapper.registerModule(new AfterburnerModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		writer = mapper.writer();
	}

	public static String obj2str(Object in) {
		try {
			if (mapper == null) init();
			if (in == null) return null;
			String s = writer.writeValueAsString(in);
			return s.intern();
		} catch (Exception e) {
			log.info("JsonUtil.obj2str() exception");
			return "";
		}
	}

	public static <T> T str2obj(String in, Class<T> valueType) {
		try {
			if (mapper == null) init();
			if (StringUtils.isEmpty(in)) return null;
			return mapper.readValue(in.intern(), valueType);
		} catch (Exception e) {
			log.info("JsonUtil.str2obj() exception");
			return null;
		}
	}

	public static Map<String, Object> str2map(String in) {
		try {
			if (mapper == null) init();
			if (StringUtils.isEmpty(in)) return null;

			TypeReference<Map<String, Object>> typeRef
					= new TypeReference<Map<String, Object>>() {
			};

			return mapper.readValue(in.intern(), typeRef);
		} catch (Exception e) {
			log.info("JsonUtil.str2map() exception");
			return new HashMap<>();
		}
	}

}
