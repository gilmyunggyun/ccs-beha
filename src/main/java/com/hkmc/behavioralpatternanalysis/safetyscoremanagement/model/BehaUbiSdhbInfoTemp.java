package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Table("BEHA_UBI_SDHB_INFO_TEMP")
public class BehaUbiSdhbInfoTemp implements Serializable {

//	private static final long serialVersionUID = 1L;
	
	// 차대(비식별화된 VIN)
	@Id
	@Column("NNID_VIN")
	private String nnidVin;
	
	// 생성일자
	@Column("CRTN_YMD")
	private String scoreDate;
	
	// 안전운전점수
	@Column("SFTY_DRIV_SCOR")
	private int safetyDrvScore;
	
	// 보험할인 가능 여부
	@Column("INSR_DSCN_PSBL_YN")
	private String insDiscountYn;
	
	// 급가속 등급
	@Column("RPAC_GRD")
	private String brstAccGrade;
	
	// 급감속 등급
	@Column("RPVL_GRD")
	private String brstDecGrade;
	
	// 심야운행 등급
	@Column("MDNG_DRIV_GRD")
	private String nightDrvGrade;
	
	// 90일간 주행거리
	@Column("F90D_RUN_DIST")
	private int rangeDrvDist;
	
	// car oid
	@Column("CAR_OID")
	private int carOid;

	// 등록일시
	@Column("RGST_DTM")
	private LocalDateTime ifDate;
}

