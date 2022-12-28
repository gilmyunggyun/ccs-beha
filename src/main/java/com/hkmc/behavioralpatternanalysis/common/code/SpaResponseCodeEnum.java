package com.hkmc.behavioralpatternanalysis.common.code;

import lombok.Getter;

@Getter
public enum SpaResponseCodeEnum {

    SUCCESS("0000", "S", "성공"),
    ERROR_S999("S999", "F", "전문형식오류"),
    ERROR_EX01("EX01", "F", "내부서버통신오류"),
    ERROR_E110("E110", "F", "Cannot found VIN"),
    ERROR_DS01("DS01", "F", "DSP연계성공이나 결과 데이터 없음"),
    ERROR_DS02("DS02", "F", "DSP 연계 실패"),
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
