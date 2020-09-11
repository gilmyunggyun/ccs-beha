package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@ToString
@Table(name="BEHA_SVDV_HIST")
@IdClass(BehaSvdvHistPK.class)
public class BehaSvdvHist implements Serializable {

	private static final long serialVersionUID = 1L;

	// 생성 일자
	@Id
	@Column(name="CRTN_YMD")
	private String ifDate;
	
	// 차대번호 (비식별화된 VIN)
	@Id
	@Column(name="NNID_VIN")
	private String nnidVin;

	// 판매 차종 코드
	@Id
	@Column(name="SALE_CAML_CD")
	private String prjVehlCd;
	
	// 양호 가혹 구분
	@Column(name="GOOD_SEVERE_SCTN")
	private String severeNormal;
	
	// 양호 운행 건수
	@Column(name="GOOD_DRIV_CNT")
	private Integer cntNormal;
	
	// 주의 운행 건수
	@Column(name="CAUTN_DRIV_CNT")
	private Integer cntCaution;
	
	@Column(name="SEVERE_DRIV_CNT")
	private Integer cntSevere;			//	가혹 운행 건수
	
	@Column(name="SEVERE_DRIV_PLUS_CNT")
	private Integer cntSeverePlus;	//	가혹 운행 플러스 건수

	@Column(name="CAR_OID")
	private Integer carOid;	//	가혹 운행 플러스 건수
}
