package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.ResponseDTO;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model.BehaSvdvHist;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;

import ccs.core.db.repository.jpa.GenericJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligenceVehicleInformationServiceImpl implements SafetyScoreManagementService {

//	@PersistenceContext
//	private EntityManager entityManager;
//	
	@SuppressWarnings({ "unused", "unchecked" })
	public ResponseEntity<ResponseDTO<Map<String, Object>>> saveIntelligenceVehicleInformation(Map<String, Object> kafkaConsumerMap) throws GlobalCCSException {
		
//		GenericJpaRepository<BehaSvdvHist , Integer> jpaRepository = new GenericJpaRepository<>(BehaSvdvHist.class, entityManager);
		
		int status = 200;
		String resultMessage = "";
		
		List<Map<String, Object>> listData = null;

		try {
			if( kafkaConsumerMap.get("listData") != null && !("".equals(kafkaConsumerMap.get("listData")))) {
				listData = (List<Map<String, Object>>) kafkaConsumerMap.get("listData");
	
		    	for(int i = 0; i < listData.size(); i++) {
		    		
		    		BehaSvdvHist behaSvdvHist = new BehaSvdvHist();
		    		
		    		behaSvdvHist.setIfDate(listData.get(i).get("crtnYmd").toString());
		    		behaSvdvHist.setNnidVin(listData.get(i).get("vin").toString());
		    		behaSvdvHist.setPrjVehlCd(listData.get(i).get("saleCamlCd").toString()); 
		    		behaSvdvHist.setSevereNormal(listData.get(i).get("goodSevereSctn").toString());
		    		behaSvdvHist.setCntNormal(Integer.valueOf(listData.get(i).get("goodDrivCnt").toString()));
		    		behaSvdvHist.setCntCaution(Integer.valueOf(listData.get(i).get("cautnDrivCnt").toString()));
		    		behaSvdvHist.setCntSevere(Integer.valueOf(listData.get(i).get("severeDrivCnt").toString()));
		    		behaSvdvHist.setCntSeverePlus(Integer.valueOf(listData.get(i).get("severeDrivPlusCnt").toString()));
	    		
//		    		jpaRepository.save(behaSvdvHist);
		    	}
			}
		} catch (Exception e) {
			log.error("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation] Ex : ", e);
			status = 500; 
			resultMessage = "An error has occurred.";
			//throw new GlobalCCSException(status, resultMessage);
		} 
		
		log.debug("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation End]");
		
		return null;
	}
}
