package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class BehaSvdvHistPK implements Serializable {

	private static final long serialVersionUID = 1L;

	// 생성 일자
	@Column(name="CRTN_YMD")
	private String ifDate;
	
	// 차대번호
	@Column(name="NNID_VIN")
	private String nnidVin;
	
	// 판매 차종 코드
	@Column(name="SALE_CAML_CD")
	private String prjVehlCd;

}
