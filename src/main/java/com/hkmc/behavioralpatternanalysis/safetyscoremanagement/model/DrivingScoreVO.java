package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class DrivingScoreVO extends SpaResponseDTO {
    private Integer status;
    private DrivingScoreResDTO drivingScoreResDTO;
}
