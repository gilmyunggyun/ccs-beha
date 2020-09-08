package com.hkmc.behavioralpatternanalysis.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/***************************************************
 * 설     명 : 응답오류 공통 포맷을 유지하기 위한 DTO
 ***************************************************/
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseErrorDTO {

	private int code;
	private String errorMessage;

}
