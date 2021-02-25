package com.hkmc.behavioralpatternanalysis.intelligenceve.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateConsumeDTO {
	private String serviceCode;

	private String subCode;

	private String sendDate;

	private long sendTotalPage;

	private long sendCurrentPage;

	private long sendTotalCount;

	private long sendPageInCount;

	@Singular("IntelligenceVehicleList")
	private List<IntelligenceDTO> IntelligenceVehicleList;

}
