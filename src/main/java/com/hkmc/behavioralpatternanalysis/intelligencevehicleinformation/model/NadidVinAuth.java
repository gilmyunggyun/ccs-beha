package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("nadidVinAuth")
public class NadidVinAuth implements Serializable {
    @Id
    private String nadidVin;
    private String nadid;
    private String getNadidVin;
    private String activeYn;
    private String advDec;
    private String telecomType;
    private String oid;
    private String carOid;
    private String brand;
    private String brandCd;
    private String carModel;
    private String fuelType;
    private String tmnlMdNm;
}
