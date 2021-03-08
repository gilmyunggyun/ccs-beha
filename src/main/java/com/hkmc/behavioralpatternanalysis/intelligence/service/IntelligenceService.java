package com.hkmc.behavioralpatternanalysis.intelligence.service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.intelligence.model.ItlBreakpadResDTO;

public interface IntelligenceService {

	/* 차량 브레이크 패드 자료에 대한 조회 요청을 처리  */
	public ItlBreakpadResDTO getItlCarBreakpadDrvScore(final String vinPath) throws GlobalCCSException;

}
