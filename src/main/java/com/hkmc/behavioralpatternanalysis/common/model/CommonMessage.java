package com.hkmc.behavioralpatternanalysis.common.model;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/***************************************************
 *설     명 : 결과 메세지 정보 객체
 ***************************************************/
@Component
@ConfigurationProperties(prefix = "common-message")
@Getter
@Setter
@ToString
public class CommonMessage {

	public static final String SWAGGER_COMMON_200_MESSAGE = "{<br/>&nbsp;&nbsp;data&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : 응답 Object<br/>&nbsp;&nbsp;resultMessage&nbsp;&nbsp; : 응답메세지(Text)<br/>}";
	public static final String SWAGGER_COMMON_500_MESSAGE = "{<br/>&nbsp;&nbsp;errorMessage&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : 에러 message<br/>}";
    private Map<String, String> messages;

}
