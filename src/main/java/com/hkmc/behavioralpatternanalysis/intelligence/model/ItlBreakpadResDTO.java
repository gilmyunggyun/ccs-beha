package com.hkmc.behavioralpatternanalysis.intelligence.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "행동패턴 가혹운행이력객체")
public class ItlBreakpadResDTO {
    @ApiModelProperty(notes = "차대번호")
    private String vin;

    @ApiModelProperty(notes = "Interface Date")
    private String ifDate;

    @ApiModelProperty(notes = "가혹운행여부")
    private String severeNormal;

    @ApiModelProperty(notes = "운행건수(양호)")
    private int cntNormal;

    @ApiModelProperty(notes = "운행건수(주의)")
    private int cntCaution;

    @ApiModelProperty(notes = "운행건수(가혹)")
    private int cntSevere;

    @ApiModelProperty(notes = "누적주행거리(주행당시 거리)")
    private int acumTrvgDist;

    public ItlBreakpadResDTO(BehaSvdvHist data, String vin, int acumTrvgDist) {
        this.vin = vin;
        this.ifDate = data.getIfDate();
        this.severeNormal = data.getSevereNormal();
        this.cntNormal = data.getCntNormal();
        this.cntCaution = data.getCntCaution();
        this.cntSevere = data.getCntSevere() + data.getCntSeverePlus();
        this.acumTrvgDist = acumTrvgDist;
    }
}
