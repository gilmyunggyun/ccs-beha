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
@ConfigurationProperties(prefix = "result-message")
@Getter
@Setter
@ToString
public class ResultMessage {

    private Map<String, String> messages;

}