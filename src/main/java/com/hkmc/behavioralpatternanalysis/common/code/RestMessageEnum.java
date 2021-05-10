package com.hkmc.behavioralpatternanalysis.common.code;

import lombok.Getter;

@Getter
public enum  RestMessageEnum {

    SUCCESS("S", "200", "정상처리되었습니다."),
    TYPE_450("F", "450", "InvaildReqSchema"),
    TYPE_595("F", "595", "InternelBizException"),
    TYPE_561("F", "561", "InternelSQLException")
    ;

    private final String resultStatus;
    private final String errCd;
    private final String errNm;

    RestMessageEnum(final String resultStatus, final String errCd, final String errNm){
        this.resultStatus = resultStatus;
        this.errCd = errCd;
        this.errNm = errNm;
    }

}
