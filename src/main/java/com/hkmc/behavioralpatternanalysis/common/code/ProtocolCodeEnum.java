package com.hkmc.behavioralpatternanalysis.common.code;

import lombok.Getter;

@Getter
public enum ProtocolCodeEnum {

    EMPTY("", ""),
//    PHONE("PHONE", "RVS-B"),
//    IOT("IOT", "RVS-C"),
//    VCS2("VCS", "RCS-V2"),
//    CC("CC", "RVS-A"),
//    VCS("VCS", "RCS-V"),
//    MO("", "RVS-V"),
//    BODY_ENGINE("", "RSC-C")
    ;

    private final String system;
    private final String protocol;

    ProtocolCodeEnum(String system, String protocol) {
        this.system = system;
        this.protocol = protocol;
    }

}
