package com.hkmc.behavioralpatternanalysis.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "SPA 공통요청객체")
public class SpaRequestDTO {

    @JsonProperty("ServiceNo")
    @ApiModelProperty(value = "서비스 구분코드", required = true, example = "F6")
    private String ServiceNo;

    @JsonProperty("CCID")
    @ApiModelProperty(value = "폰 구분 식별자", required = true, example = "00000000-3f90-1882-0033-c58700000000_BLU")
    private String CCID;

    @JsonProperty("carID")
    @ApiModelProperty(value = "차량ID", required = true, example = "f51916cd-e875-45b1-beaf-b51b6295d814")
    private String carID;

    private String fromHost;

}
