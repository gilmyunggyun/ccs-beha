package com.hkmc.behavioralpatternanalysis.common.code;

import lombok.Getter;

@Getter
public enum SpaResponseCodeEnum {

//    SUCCESS("0000", "S", "성공"),
//
//    WATING_1111("1111", "W", "대기상태"), // pollcarstatus
//    WATING_7777("7777", "W", "대기상태"), // pollcoordioption
//    TIMEOUT("6666", "F", "타임아웃"),
//
    ERROR_S999("S999", "F", "전문형식오류"),
    ERROR_EX01("EX01", "F", "내부서버통신오류"),
//    ERROR_E500("E500", "F", "단말-차량간 통신에러"), // pollcarstatus
//    ERROR_E700("E700", "F", "제어요청 실패"), // pollcoordioption
//    ERROR_EEEE("EEEE", "F", "제어요청 실패"), // pollcoordioption
//    ERROR_1111("1111", "F", "센터에 저장된 차량상태 값이 없음"),
//    ERROR_7777("7777", "F", "위경도 미전송"),
//    ERROR_7778("7778", "F", "폰-차량간 거리가 1km 이상 or 주차위치찾기 거리 초과"),
//    ERROR_8888("8888", "F", "세션정보가 존재하지 않음"),
//    ERROR_S888("S888", "F", "충전 정보 미수신")
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
