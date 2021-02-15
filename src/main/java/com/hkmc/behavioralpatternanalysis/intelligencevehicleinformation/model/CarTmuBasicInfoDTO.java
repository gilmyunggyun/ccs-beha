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
@RedisHash("carTmuBasicInfo")
public class CarTmuBasicInfoDTO implements Serializable {
    @Id
    private String vin;
    private String nadid;
    private String brand;
    private String tmnlMdlNm;
    private String telecomType;
    private String advDec;
    private String imei;
    private String meid;
}
