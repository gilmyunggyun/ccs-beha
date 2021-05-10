package com.hkmc.behavioralpatternanalysis.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter@ToString
@Builder
public class RestErrorDTO {

    private String resultStatus;
    private String errCd;
    private String errNm;

}
