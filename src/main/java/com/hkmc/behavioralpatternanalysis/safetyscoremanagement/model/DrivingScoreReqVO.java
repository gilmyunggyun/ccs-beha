package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import lombok.*;

import java.util.Map;
import java.util.Optional;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrivingScoreReqVO {
    private String vinPath;
    private Map<String, String> header;
    private DrivingScoreReqDTO drivingScoreReqDTO;

}
