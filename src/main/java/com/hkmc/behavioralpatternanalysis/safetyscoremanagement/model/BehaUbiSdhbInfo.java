package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@ToString
@Table(name="BEHA_UBI_SDHB_INFO")
public class BehaUbiSdhbInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	// 차대(비식별화된 VIN)
	@Id
	@Column(name="NNID_VIN")
	private String nnidVin;
	
	// 생성일자
	@Column(name="CRTN_YMD")
	private String scoreDate;
	
	// 안전운전점수
	@Column(name="SFTY_DRIV_SCOR")
	private int safetyDrvScore;
	
	// 보험할인 가능 여부
	@Column(name="INSR_DSCN_PSBL_YN")
	private String insDiscountYn;
	
	// 급가속 등급
	@Column(name="RPAC_GRD")
	private String brstAccGrade;
	
	// 급감속 등급
	@Column(name="RPVL_GRD")
	private String brstDecGrade;
	
	// 심야운행 등급
	@Column(name="MDNG_DRIV_GRD")
	private String nightDrvGrade;
	
	// 90일간 주행거리
	@Column(name="F90D_RUN_DIST")
	private int rangeDrvDist;
	
	// 등록일시
	@Column(name="RGST_DTM")
	private Date ifDate;
}

