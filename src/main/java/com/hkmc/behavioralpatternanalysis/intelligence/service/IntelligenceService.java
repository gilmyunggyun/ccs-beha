package com.hkmc.behavioralpatternanalysis.intelligence.service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.intelligence.model.ItlBreakpadResDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface IntelligenceService {

	// 소비자 행통패턴 분석 - 지능형 차량관리 Kafka 처리
	public void saveIntelligence(ConsumerRecord<String, String> kafkaConsumer) throws GlobalCCSException;

	/* 차량 브레이크 패드 자료에 대한 조회 요청을 처리  */
	public ItlBreakpadResDTO getItlCarBreakpadDrvScore(final String vinPath) throws GlobalCCSException;

}
