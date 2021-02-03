package com.hkmc.behavioralpatternanalysis.common.code;

import lombok.Getter;

@Getter
public enum SpaResponseCodeEnum {

    SUCCESS("0000", "S", "성공"),
    ERROR_S999("S999", "F", "전문형식오류"),
    ERROR_S504("S504", "F", "센터에 의해 계정 접속 차단됨"),
    ERROR_S510("S510", "F", "유효하지 않은 SID (SID 만료)"),
    ERROR_ES01("ES01", "F", "ESB연계 성공, 결과데이터 없음"),
    ERROR_ES02("ES02", "F", "ESB연계 실패 (찾아가는 충전 서비스 쪽 연계하는 시스템 문제)"),
    ERROR_CC01("CC01", "F", "차량 제어 권한 없음(CCSP VEHICLE SERVIER 4002 에러)"),
    ERROR_CC03("CC03", "F", "개인정보 설정변경 중"),
    ERROR_CC04("CC04", "F", "개인정보 OFF 설정임"),

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
