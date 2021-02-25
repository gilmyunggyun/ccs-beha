package com.hkmc.behavioralpatternanalysis.intelligence.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItlBreakpadResDTO {
    private List<BehaSvdvHist> body;
    private String resultStatus;
    private String message;
}
