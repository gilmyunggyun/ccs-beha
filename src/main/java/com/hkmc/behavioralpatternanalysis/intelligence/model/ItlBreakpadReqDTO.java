package com.hkmc.behavioralpatternanalysis.intelligence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItlBreakpadReqDTO {
    @JsonProperty("vin")
    @ApiModelProperty(value = "차량 ID", required = true, example = "vinPath")
    private String vin;
}
