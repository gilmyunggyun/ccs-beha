package com.hkmc.behavioralpatternanalysis.common.code;

import com.hkmc.behavioralpatternanalysis.common.Const;
import io.netty.util.internal.StringUtil;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public enum BehavioralPatternAnalysisServiceEnum {

    EMPTY(StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING),
    UBI_SCORE("UBISCORE", "/api/v1/dsp/ubi", Const.ServiceNo.UBI_SCORE, null),
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
