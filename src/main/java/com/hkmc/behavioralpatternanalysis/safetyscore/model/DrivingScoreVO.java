package com.hkmc.behavioralpatternanalysis.safetyscore.model;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrivingScoreVO {
    private String vinPath;
    private Map<String, String> header;
    private DrivingScoreReqDTO drivingScoreReqDTO;

}
