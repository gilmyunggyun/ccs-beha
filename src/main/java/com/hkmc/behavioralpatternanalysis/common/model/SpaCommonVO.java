package com.hkmc.behavioralpatternanalysis.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SpaCommonVO {

    @JsonProperty("ServiceNo")
    private String ServiceNo;
    @JsonProperty("CCID")
    private String CCID;
    @JsonProperty("carID")
    private String carID;
    private String ctrl;
    private String fromHost;

}
