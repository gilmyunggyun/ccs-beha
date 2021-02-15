package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(IntelligenceVehiclePK.class)
public class IntelligenceVehicleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 생성 일자 */
	@Id
	private String ifDate;

	/** 차대번호 */
	@Id
	private String nnidVin;

	/** 판매 차종 코드 */
	private String prjVehlCd;

	/** 양호 가혹 구분 */
	private String severeNormal;

	/** 양호 운행 건수 */
	private int cntNormal;

	/** 주의 운행 건수 */
	private int cntCaution;

	/** 가혹 운행 건수 */
	private int cntSevere;

	/** 가혹 운행 플러스 건수 */
	private int cntSeverePlus;

}
