package com.hkmc.behavioralpatternanalysis.common.code;

import lombok.Getter;

@Getter
public enum SpaResponseCodeEnum {

    SUCCESS("0000", "S", "성공"),
    ERROR_S999("S999", "F", "전문형식오류"),
    ERROR_EX01("EX01", "F", "내부서버통신오류"),
    ERROR_E110("E110", "F", "Cannot found VIN"),
    ;
    private final String resCode;
    private final String retCode;
    private final String message;

    SpaResponseCodeEnum(final String resCode, final String retCode, final String message){
        this.resCode = resCode;
        this.retCode = retCode;
        this.message = message;
    }

}
