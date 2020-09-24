package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service;

import java.util.Map;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;

public interface IntelligenceVehicleInformationService {

	// 소비자 행통패턴 분석 - 지능형 차량관리 Kafka 처리
	public void saveIntelligenceVehicleInformation(Map<String, Object> kafkaConsumerMap) throws GlobalCCSException;
	
	/* 차량 브레이크 패드 자료에 대한 조회 요청을 처리  */
	public Map<String, Object> itlCarBreakpadDrvScoreSearch(Map<String, Object> reqBody) throws GlobalCCSException;

}
