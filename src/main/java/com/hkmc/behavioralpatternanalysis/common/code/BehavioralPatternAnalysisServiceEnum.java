package com.hkmc.behavioralpatternanalysis.common.code;

import io.netty.util.internal.StringUtil;
import lombok.Getter;

@Getter
public enum BehavioralPatternAnalysisServiceEnum {

    EMPTY(StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING),
//    CARSTATUS("CARSTATUS", "/vhc/rept", Const.ServiceNo.CAR_STATUS, BigInteger.ONE.toString()),
//    CARSTATUS_TO_IOT("IOT", "/iot/stat/res", StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING),
//    CARSTATUS_TO_VCS("VCS", "/app/cmm/vsr", StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING)
    ;

    private final String serviceName;
    private final String serviceUrl;
    private final String serviceNo;
    private final String ctrl;

    BehavioralPatternAnalysisServiceEnum(String serviceName, String serviceUrl, String serviceNo, String ctrl) {
        this.serviceName = serviceName;
        this.serviceUrl = serviceUrl;
        this.serviceNo = serviceNo;
        this.ctrl = ctrl;
    }

}
