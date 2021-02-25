package com.hkmc.behavioralpatternanalysis.safetyscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hkmc.behavioralpatternanalysis.common.model.SpaRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DrivingScoreReqDTO extends SpaRequestDTO {
    @JsonProperty("mtsNo")
    @ApiModelProperty(value = "MTS: 단말전화번호 (앱 고정값)", required = true, example = "MTS")
    private String mtsNo;
}
