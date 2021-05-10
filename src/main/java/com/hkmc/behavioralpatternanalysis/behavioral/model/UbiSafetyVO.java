package com.hkmc.behavioralpatternanalysis.behavioral.model;

import lombok.*;

import java.util.Map;

@Getter@Setter@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UbiSafetyVO {

    private String vinPath;
    private Map<String, String> header;
    private UbiSafetyReqDTO ubiSafetyReq;

}
