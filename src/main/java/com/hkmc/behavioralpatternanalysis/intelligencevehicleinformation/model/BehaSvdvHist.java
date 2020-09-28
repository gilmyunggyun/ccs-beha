package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@ApiModel(description = "행동패턴 가혹운행이력객체")
@Table("BEHA_SVDV_HIST")
public class BehaSvdvHist implements Serializable {

//	private static final long serialVersionUID = 1L;

	@Id
	@ApiModelProperty(notes = "생성 일자")
	@Column("CRTN_YMD")
	private String ifDate;
	
	@ApiModelProperty(notes = "비식별 차대번호")
	@Column("NNID_VIN")
	private String nnidVin;

	@ApiModelProperty(notes = "판매 차종 코드")
	@Column("SALE_CAML_CD")
	private String prjVehlCd;
	
	@ApiModelProperty(notes = "양호 가혹 구분")
	@Column("GOOD_SEVERE_SCTN")
	private String severeNormal;
	
	@ApiModelProperty(notes = "양호 운행 건수")
	@Column("GOOD_DRIV_CNT")
	private int cntNormal;
	
	@ApiModelProperty(notes = "주의 운행 건수")
	@Column("CAUTN_DRIV_CNT")
	private int cntCaution;
	
	@ApiModelProperty(notes = "가혹 운행 건수")
	@Column("SEVERE_DRIV_CNT")
	private int cntSevere;
	
	@ApiModelProperty(notes = "가혹 운행 플러스 건수")
	@Column("SEVERE_DRIV_PLUS_CNT")
	private int cntSeverePlus;

	@ApiModelProperty(notes = "CAR_OID")
	@Column("CAR_OID")
	private int carOid;
}
