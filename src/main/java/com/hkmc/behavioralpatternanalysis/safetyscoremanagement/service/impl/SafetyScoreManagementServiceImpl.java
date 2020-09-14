package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.ResponseDTO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SafetyScoreManagementServiceImpl implements SafetyScoreManagementService {

//	private final PostgreService postgreSrv;	
	
//	private final GenericRedisRepository<RedisVin, String> redisVinRepo;
	
	/**
	 * UBI 안전 운전 점수 조회
	 * 
	 * @param inUri
	 * @param bodyMap
	 * @param reqHeader
	 * @return
	 * @throws GlobalCCSException
	 */
	@Override
	public ResponseEntity<ResponseDTO<Map<String, Object>>> ubiSafetyDrivingScoreSearch (String inUri, Map<String, Object> bodyMap, Map<String, String> reqHeader) throws GlobalCCSException {
		if (log.isDebugEnabled()) {
			log.debug("[[ UbiSafetyDrivingScoreServiceImpl-ubiSafetyDrivingScoreSearch ]]");
		}
		
		int status = 200;
		String resultMessage = "Success";	
		
		MultiValueMap<String, String> resHeader = null;
		Map<String, Object> body = new HashMap<String, Object>();
		
		String vin = "";
		
		try {        	
			vin = String.valueOf(bodyMap.get("vin"));
			resHeader = setReqHeaderToResHeader(reqHeader);

		} catch (Exception e) {
			log.error("1.Ex:", e);
			
			status = 500; 
			resultMessage = "There is no parameter information.";
			throw new GlobalCCSException(status, resultMessage);
		} 		
		
		if (log.isDebugEnabled()) {
			log.debug("inUri = [{}], vin = [{}], reqHeader = [{}]", inUri, vin, reqHeader);
		}
		
        try {
//        	SafetyScoreManagement reqDto = new SafetyScoreManagement();
        	
			String srchPatt = "*_" + bodyMap.get("vin").toString();
//			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);
        	
	        String query = "SELECT "
	        		+ "			vin, crtn_ymd, f90d_run_dist, insr_dscn_psbl_yn, mdng_driv_grd, rgst_dtm, rpac_grd, rpvl_grd, sfty_driv_scor"
	        		+ "		FROM BEHA_UBI_SDHB_INFO"
	        		+ "		WHERE ('x'||lpad(SUBSTR(vin, 17, LENGTH(vin) - 19), 8, '0'))\\:\\:bit(32)\\:\\:integer = CAST(:param AS INTEGER)"
	        		+ "		AND LENGTH(vin) >= 25"
	        		;

//	        UbiSafetyDrivingScore resDto = postgreSrv.findNativeQuery(query, reqDto, receiveRedisVinData.get(0).getCarOid());
	        
//	        body.put("body", resDto);
	        body.put("resultStatus", "S");
	        body.put("status", status);
	        body.put("message", resultMessage);	        
		} catch (Exception e) {
			log.error("3.Ex:", e);
			
			status = 500; 
			resultMessage = "An error has occurred.";
			throw new GlobalCCSException(status, resultMessage);
		} 
        
        if (log.isDebugEnabled()) {
        	log.debug("[[{}]] Response inUri = [{}], hData = [{}]", vin, inUri, body.toString());
        }      
        
   		return new ResponseEntity<>(
   				ResponseDTO.<Map<String, Object>>builder().code(status).data(body).resultMessage(resultMessage).build(), 
   				resHeader,
   				HttpStatus.resolve(status) == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.resolve(status));		
	}	
	
	
	/**
	 * UBI 안전 운전 점수 삭제
	 * 
	 * @param inUri
	 * @param bodyMap
	 * @param reqHeader
	 * @return
	 * @throws GlobalCCSException
	 */
	@Override
	@Transactional
	public ResponseEntity<ResponseDTO<Map<String, Object>>> ubiSafetyDrivingScoreDelete (String inUri, Map<String, Object> bodyMap, Map<String, String> reqHeader) throws GlobalCCSException {
		if (log.isDebugEnabled()) {
			log.debug("[[ UbiSafetyDrivingScoreServiceImpl-ubiSafetyDrivingScoreDelete ]]");
		}
		
		int status = 200;
		String resultMessage = "";
		MultiValueMap<String, String> resHeader = null;
		Map<String, Object> body = new HashMap<String, Object>();
		
		String vin = "";
		
		try {        	
			vin = String.valueOf(bodyMap.get("vin"));
			resHeader = setReqHeaderToResHeader(reqHeader);
		} catch (Exception e) {
			log.error("1.Ex:", e);
			
			status = 500; 
			resultMessage = "There is no parameter information.";
			throw new GlobalCCSException(status, resultMessage);
		} 
		
		if (log.isDebugEnabled()) {
			log.debug("inUri = [{}], vin = [{}], reqHeader = [{}]", inUri, vin, reqHeader);
		}
		
        try {
        	
//        	if (postgreSrv.existsById(new UbiSafetyDrivingScore(), vin)) {
//        	
//	        	postgreSrv.deleteById(new UbiSafetyDrivingScore(), vin);
//        		
//        	} else {       		
//        		log.debug("2.Ex: There is no data.");
//        		
//				status = 500;
//				resultMessage = "There is no data.";
//				throw new GlobalCCSException(status, resultMessage);
//			}
        	
		} catch (GlobalCCSException ge) {
			status = 500; 
			resultMessage = ge.getErrorMessage();			
			throw new GlobalCCSException(status, resultMessage);
		} catch (Exception e) {
			log.error("3.Ex:", e);
			
			status = 500; 
			resultMessage = "An error has occurred.";
			throw new GlobalCCSException(status, resultMessage);
		} 
        
        if (log.isDebugEnabled()) {
        	log.debug("[[{}]] Response inUri = [{}]", vin, inUri);
        }     
        
   		return new ResponseEntity<>(
   				ResponseDTO.<Map<String, Object>>builder().code(status).data(body).resultMessage(resultMessage).build(), 
   				resHeader,
   				HttpStatus.resolve(status) == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.resolve(status));		
	}
	
	
	/**
	 * RequestHeader 정보를 ResponseHeader 에 세팅하는 함수 
	 * @param reqHeader
	 * @param status
	 * @param resultMessage
	 * @return
	 */
	public MultiValueMap<String, String> setReqHeaderToResHeader(Map<String, String> reqHeader) throws Exception {
		
		MultiValueMap<String, String> resHeader = new LinkedMultiValueMap<>();
		
		//header data
		resHeader.add("transactionId", 	reqHeader.get("transactionId"));
		resHeader.add("requestSystemId",reqHeader.get("requestSystemId"));
		resHeader.add("instanceId", 	reqHeader.get("instanceId"));
		resHeader.add("direction", 		reqHeader.get("direction"));
		resHeader.add("resultCode",  	reqHeader.get("resultCode"));
		resHeader.add("resultMessage", 	reqHeader.get("resultMessage"));

		return resHeader;
	}	
}
