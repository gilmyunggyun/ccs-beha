package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service;

import java.util.Map;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;

public interface SafetyScoreManagementService_OLD {

	// 소비자 행통패턴 분석 - 안전운행점수
	public void saveSafetyScoreManagement(Map<String, Object> kafkaConsumerMap) throws GlobalCCSException;
	
	// UBI 안전 운전 점수 조회
	public Map<String, Object> ubiSafetyDrivingScoreSearch(Map<String, Object> reqBody) throws GlobalCCSException;
	
	// UBI 안전 운전 점수 삭제
	public Map<String, Object> ubiSafetyDrivingScoreDelete(Map<String, Object> reqBody) throws GlobalCCSException;

}
