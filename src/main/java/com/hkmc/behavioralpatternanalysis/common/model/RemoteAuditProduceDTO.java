package com.hkmc.behavioralpatternanalysis.common.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RemoteAuditProduceDTO {

    private String svcId;
    private String ccid;
    private String vin;
    private String nadId;
    private String xtid;
    private String tid;
    private String resultCode;
    private String resultMessage;
    private String serviceName;
    private String fromHost;
    private String toHost;
    private String transactionType;
    private String svcUrl;
    private String svcStartTime;
    private String svcEndTime;

    private String ctrl;
    private String svcStatusCd;
    private String msgSeq;

    private String appMode;
    private String brandCd;

}
