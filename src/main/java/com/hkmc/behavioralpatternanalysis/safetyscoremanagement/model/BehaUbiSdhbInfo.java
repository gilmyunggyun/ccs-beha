package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@ApiModel(description = "행동패턴 UBI안전운전습관정보객체")
@Table("BEHA_UBI_SDHB_INFO")
public class BehaUbiSdhbInfo implements Serializable {

//	private static final long serialVersionUID = 1L;
	
	@Id
	@ApiModelProperty(notes = "비식별 차대번호")
	@Column("NNID_VIN")
	private String nnidVin;
	
	@ApiModelProperty(notes = "생성일자")
	@Column("CRTN_YMD")
	private String scoreDate;
	
	@ApiModelProperty(notes = "안전운전점수")
	@Column("SFTY_DRIV_SCOR")
	private int safetyDrvScore;
	
	@ApiModelProperty(notes = "보험할인 가능 여부")
	@Column("INSR_DSCN_PSBL_YN")
	private String insDiscountYn;
	
	@ApiModelProperty(notes = "급가속 등급")
	@Column("RPAC_GRD")
	private String brstAccGrade;
	
	@ApiModelProperty(notes = "급감속 등급")
	@Column("RPVL_GRD")
	private String brstDecGrade;
	
	@ApiModelProperty(notes = "심야운행 등급")
	@Column("MDNG_DRIV_GRD")
	private String nightDrvGrade;
	
	@ApiModelProperty(notes = "90일간 주행거리")
	@Column("F90D_RUN_DIST")
	private int rangeDrvDist;
	
	@ApiModelProperty(notes = "CAR_OID")
	@Column("CAR_OID")
	private int carOid;
	
	@ApiModelProperty(notes = "등록일시")
	@Column("RGST_DTM")
	private LocalDateTime ifDate;
}

