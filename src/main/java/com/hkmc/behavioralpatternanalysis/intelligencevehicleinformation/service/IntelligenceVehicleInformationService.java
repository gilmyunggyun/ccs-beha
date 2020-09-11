package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.ResponseDTO;

public interface IntelligenceVehicleInformationService {

	// 소비자 행통패턴 분석 - 지능형 차량관리
	public ResponseEntity<ResponseDTO<Map<String, Object>>> saveIntelligenceVehicleInformation(Map<String, Object> kafkaConsumerMap) throws GlobalCCSException;
}
