package com.hkmc.behavioralpatternanalysis.behavioral.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hkmc.behavioralpatternanalysis.common.model.SpaRequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(description = "안전운전점수 요청 객체")
public class UbiSafetyReqDTO extends SpaRequestDTO {

    @JsonProperty("mtsNo")
    @ApiModelProperty(value = "MTS: 단말전화번호 (앱 고정값)", required = true, example = "MTS")
    private String mtsNo;

}
