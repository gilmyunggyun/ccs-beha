package com.hkmc.behavioralpatternanalysis.itlCar.service;

import java.util.Map;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;

public interface ItlCarBreakpadDrivingScoreService {
	
	/* 차량 브레이크 패드 자료에 대한 조회 요청을 처리  */
	public Map<String, Object> itlCarBreakpadDrvScoreSearch(Map<String, Object> reqBody) throws GlobalCCSException;
}
