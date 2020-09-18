package com.hkmc.behavioralpatternanalysis.itlCar.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("BEHA_SVDV_HIST")
public class ItlCarBreakpadDrivingScore implements Serializable {

	private static final long serialVersionUID = 1L;

	// 생성 일자
	@Id
	@Column("CRTN_YMD")
	private String ifDate;
	
	// 차대번호 (비식별화된 VIN)
	@Column("NNID_VIN")
	private String nnidVin;

	// 판매 차종 코드
	@Column("SALE_CAML_CD")
	private String prjVehlCd;
	
	// 양호 가혹 구분
	@Column("GOOD_SEVERE_SCTN")
	private String severeNormal;
	
	// 양호 운행 건수
	@Column("GOOD_DRIV_CNT")
	private Integer cntNormal;
	
	// 주의 운행 건수
	@Column("CAUTN_DRIV_CNT")
	private Integer cntCaution;
	
	// 가혹 운행 건수
	@Column("SEVERE_DRIV_CNT")
	private Integer cntSevere;			
	
	// 가혹 운행 플러스 건수
	@Column("SEVERE_DRIV_PLUS_CNT")
	private Integer cntSeverePlus;	

	// car_oid
	@Column("CAR_OID")
	private Integer carOid;
}
