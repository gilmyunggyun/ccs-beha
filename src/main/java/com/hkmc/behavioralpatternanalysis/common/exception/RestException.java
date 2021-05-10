package com.hkmc.behavioralpatternanalysis.common.exception;

import com.hkmc.behavioralpatternanalysis.common.code.RestMessageEnum;
import com.hkmc.behavioralpatternanalysis.common.model.RestErrorDTO;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
public class RestException extends RuntimeException {

    private RestErrorDTO restError;

    public RestException() {
        this.restError = RestErrorDTO.builder()
                .resultStatus(RestMessageEnum.TYPE_595.getResultStatus())
                .errCd(RestMessageEnum.TYPE_595.getErrCd())
                .errNm(RestMessageEnum.TYPE_595.getErrNm())
                .build();
    }

    public RestException(String errCd) {
        RestMessageEnum restMessage = RestMessageEnum.valueOf("TYPE_"+errCd);

        if (ObjectUtils.isEmpty(restMessage)) {
            restMessage = RestMessageEnum.valueOf("TYPE_595");
        }

        this.restError = RestErrorDTO.builder()
                .resultStatus(restMessage.getResultStatus())
                .errCd(restMessage.getErrCd())
                .errNm(restMessage.getErrNm())
                .build();
    }

}
