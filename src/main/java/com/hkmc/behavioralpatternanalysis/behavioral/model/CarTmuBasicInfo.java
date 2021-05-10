package com.hkmc.behavioralpatternanalysis.behavioral.model;

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
public class CarTmuBasicInfo implements Serializable {
    @Id
    private String vin;
    private String carOid;

}
