package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Table("BEHA_SVDV_HIST")
public class BehaSvdvHist implements Serializable {

//	private static final long serialVersionUID = 1L;

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
	private int cntNormal;
	
	// 주의 운행 건수
	@Column("CAUTN_DRIV_CNT")
	private int cntCaution;
	
	// 가혹 운행 건수
	@Column("SEVERE_DRIV_CNT")
	private int cntSevere;
	
	// 가혹 운행 플러스 건수
	@Column("SEVERE_DRIV_PLUS_CNT")
	private int cntSeverePlus;

	// car oid
	@Column("CAR_OID")
	private int carOid;
}
