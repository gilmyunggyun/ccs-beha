package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service;

import java.util.Map;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;

public interface IntelligenceVehicleInformationService {
	
	public Map<String, Object> itlCarBreakpadDrvScore(Map<String, Object> reqBody) throws GlobalCCSException;
}
