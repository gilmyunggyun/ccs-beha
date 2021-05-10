package com.hkmc.behavioralpatternanalysis.behavioral.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter@Setter@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "행동패턴 가혹운행이력객체")
@JsonInclude(Include.NON_NULL)
public class ItlBreakpadResDTO {

    @ApiModelProperty(notes = "차대번호")
    private String vin;

    @ApiModelProperty(notes = "Interface Date")
    private String ifDate;

    @ApiModelProperty(notes = "가혹운행여부")
    private String severeNormal;

    @ApiModelProperty(notes = "운행건수(양호)")
    private Integer cntNormal;

    @ApiModelProperty(notes = "운행건수(주의)")
    private Integer cntCaution;

    @ApiModelProperty(notes = "운행건수(가혹)")
    private Integer cntSevere;

    @ApiModelProperty(notes = "누적주행거리(주행당시 거리)")
    private Integer acumTrvgDist;

    @ApiModelProperty(notes = "수행 결과/상태")
    private String resultStatus;
    @ApiModelProperty(notes = "exception 시 esb error code")
    private String errCd;
    @ApiModelProperty(notes = "exception 시 esb error 내용")
    private String errNm;

}
