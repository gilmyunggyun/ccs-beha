package com.hkmc.behavioralpatternanalysis.common.model;

import lombok.Getter;

/*
보험사별 할인 여부 정보 추가
현대해상화재보험: 70점 이상 / 1,000km 이상 (14% 할인) Hyundai
KB손해보험: 70점 이상 / 1,000km 이상 (12.3% 할인) KB
한화손해보험: 60점 이상 / 1,000km 이상 (60점 이상 6% / 70점 이상 13% 할인) Hanhwa
AXA손해보험: 70점 / 500km 이상 AXA
하나손해보험: 70점 이상 / 1,000km 이상 (8% 할인) Hana
*/

@Getter
public enum InsuranceEnum {
    HANHWA("Hanhwa", 60, 1000),
    KB("KB", 70, 1000),
    AXA("AXA", 70, 500),
    HANA("Hana", 70, 1000),
    HYUNDAI("Hyundai", 70, 1000);

    private final String cName;
    private final Integer drivingScore;
    private final Integer drvDistance;

    InsuranceEnum(final String cName, final Integer drivingScore, final Integer drvDistance){
        this.cName = cName;
        this.drivingScore = drivingScore;
        this.drvDistance = drvDistance;
    }
}
