package com.hkmc.behavioralpatternanalysis.common.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@RedisHash("nadidVinAuth")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RedisVin implements Serializable {

	@Id
	public String nadidVin;	// nadid + '_' + vin

	public String nadid;
	
	public String vin;
	
	public String telecomType;
	
	public String advDec;
	
	public String brandCd;
	
	private String carModel;
	
	public String fuelType;
	
	public long oid;
	
	public String activeYn;
	
	public String carOid;
	
	public String tcuOid;
}
