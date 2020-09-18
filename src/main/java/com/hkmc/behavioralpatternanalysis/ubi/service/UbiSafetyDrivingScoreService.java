package com.hkmc.behavioralpatternanalysis.ubi.service;

import java.util.Map;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;

public interface UbiSafetyDrivingScoreService {

	/* UBI 안전 운전 점수 조회  */
	public Map<String, Object> ubiSafetyDrivingScoreSearch(Map<String, Object> reqBody) throws GlobalCCSException;
	
	/* UBI 안전 운전 점수 삭제 */
	public Map<String, Object> ubiSafetyDrivingScoreDelete (Map<String, Object> reqBody) throws GlobalCCSException;
	
}
