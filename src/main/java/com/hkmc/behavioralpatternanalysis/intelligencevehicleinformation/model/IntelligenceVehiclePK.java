package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode
public class IntelligenceVehiclePK implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 생성 일자 */
	@Id
	@Column(name = "if_date")
	private String ifDate;

	/** 차대번호 */
	@Id
	@Column(name = "vin")
	private String nnidVin;

}
