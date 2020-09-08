package com.hkmc.behavioralpatternanalysis.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/***************************************************
 * 설     명 : 응답시 공통 포맷을 유지하기 위한 DTO
 ***************************************************/
@Getter
@Builder
@ToString
public class ResponseDTO<T> {

    private T data;
    private Integer code;
    private String resultMessage;

}

