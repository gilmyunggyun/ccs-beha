package com.hkmc.behavioralpatternanalysis.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.ResponseDTO;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.IntelligenceVehicleInformationService;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "소비자 행동패턴 분석 서비스")
@RequestMapping(Const.BehavioralPatternAnalysis.VERSION_V1)
public class BehavioralPatternAnalysisController {

	/*	차량 브레이크 패드 자료에 대한 조회 요청	*/
	@Autowired
	private IntelligenceVehicleInformationService intelligenceVehicleInformationService;
	
	/*	UBI 안전운전점수 수신,제공,처리	*/
	@Autowired
	private SafetyScoreManagementService safetyScoreManagementService;


	/**
	 * itlCarBreakpadDrvScore 차량 브레이크 패드 자료에 대한 조회 요청을 처리
	 * @param 	HttpHeaders header, Map<String, Object> body, HttpServletRequest req
	 * @return	ResponseEntity<>
	 * @throws 	GlobalCCSException
	 */
	@PostMapping(value="/itlCarBreakpadDrvScoreSearch")
	public ResponseEntity<ResponseDTO<Map<String, Object>>> itlCarBreakpadDrvScoreSearch(@RequestHeader HttpHeaders header, @RequestBody Map<String, Object> body, HttpServletRequest req) throws GlobalCCSException {
		
		Map<String, Object> resultData = intelligenceVehicleInformationService.itlCarBreakpadDrvScoreSearch(body);
    	
   		return new ResponseEntity<>(
   				ResponseDTO.<Map<String, Object>>builder().code(Integer.parseInt(resultData.get("status").toString())).data(resultData).resultMessage(resultData.get("message").toString()).build(), 
   				HttpStatus.resolve(Integer.parseInt(resultData.get("status").toString())) == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.resolve(Integer.parseInt(resultData.get("status").toString())));

    }

	
	/**
	 * ubiSafetyDrivingScoreSearch UBI 안전 운전 점수 조회
	 * @param 	HttpHeaders header, Map<String, Object> body, HttpServletRequest req
	 * @return	ResponseEntity<>
	 * @throws 	GlobalCCSException
	 */
	@PostMapping(value="/ubiSafetyDrivingScoreSearch")
	public ResponseEntity<ResponseDTO<Map<String, Object>>> ubiSafetyDrivingScoreSearch(@RequestHeader HttpHeaders header, @RequestBody Map<String, Object> body, HttpServletRequest req) throws GlobalCCSException {

		Map<String, Object> resultData = safetyScoreManagementService.ubiSafetyDrivingScoreSearch(body);
		
   		return new ResponseEntity<>(
   				ResponseDTO.<Map<String, Object>>builder().code(Integer.parseInt(resultData.get("status").toString())).data(resultData).resultMessage(resultData.get("message").toString()).build(), 
   				HttpStatus.resolve(Integer.parseInt(resultData.get("status").toString())) == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.resolve(Integer.parseInt(resultData.get("status").toString())));

    }
    
	
    /**
     * ubiSafetyDrivingScoreDelete	UBI 안전 운전 점수 조회
     * @param 	HttpHeaders header, Map<String, Object> body, HttpServletRequest req
     * @return	ResponseEntity<>
     * @throws 	GlobalCCSException
     */
    @PostMapping(value="/ubiSafetyDrivingScoreDelete")
	public ResponseEntity<ResponseDTO<Map<String, Object>>> ubiSafetyDrivingScoreDelete(@RequestHeader HttpHeaders header, HttpServletRequest req, @RequestBody Map<String, Object> body) throws GlobalCCSException {
    	
    	Map<String, Object> resultData = safetyScoreManagementService.ubiSafetyDrivingScoreDelete(body);
    	
   		return new ResponseEntity<>(
   				ResponseDTO.<Map<String, Object>>builder().code(Integer.parseInt(resultData.get("status").toString())).data(resultData).resultMessage(resultData.get("message").toString()).build(), 
   				HttpStatus.resolve(Integer.parseInt(resultData.get("status").toString())) == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.resolve(Integer.parseInt(resultData.get("status").toString())));

    }

}
