package com.hkmc.behavioralpatternanalysis.controller;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreReqDTO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreReqVO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreVO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.ResponseDTO;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.IntelligenceVehicleInformationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "소비자 행동패턴 분석 서비스")
@RequestMapping(Const.BehavioralPatternAnalysis.VERSION_V1)
public class BehavioralPatternAnalysisController {

	@Autowired
	private IntelligenceVehicleInformationService intelligenceVehicleInformationService;
	
	@Autowired
	private SafetyScoreManagementService safetyScoreManagementService;

	@ApiOperation(value = "차량 브레 이크 패드 자료에 대한 조회 요청을 처리")
	@PostMapping(value="/itlCarBreakpadDrvScoreSearch")
	public ResponseEntity<ResponseDTO<Map<String, Object>>> itlCarBreakpadDrvScoreSearch (
			@RequestHeader HttpHeaders header,
			@RequestBody Map<String, Object> body,
			HttpServletRequest req
	) throws GlobalCCSException {
		
		Map<String, Object> resultData = intelligenceVehicleInformationService.itlCarBreakpadDrvScoreSearch(body);

		return ResponseEntity
				.status(Optional
						.ofNullable(HttpStatus.resolve(Integer.parseInt(resultData.get("status").toString())))
						.orElse(HttpStatus.INTERNAL_SERVER_ERROR))
				.body(ResponseDTO.<Map<String, Object>>builder()
						.code(Integer.parseInt(resultData.get("status").toString()))
						.data(resultData)
						.resultMessage(resultData.get("message").toString()).build());
    }

	@ApiOperation(value = "UBI 안전 운전 점수 조회")
	@PostMapping(value="/ubi/score/{vinPath}")
	public ResponseEntity<?> getUbiSafetyDrivingScore (
			@RequestHeader HttpHeaders headers,
			@RequestBody DrivingScoreReqDTO body,
			@PathVariable("vinPath") String vinPath
	) {

		DrivingScoreVO drivingScoreVO = safetyScoreManagementService.ubiSafetyDrivingScoreRequest(
				DrivingScoreReqVO.builder()
						.drivingScoreReqDTO(body)
						.vinPath(vinPath)
						.header(headers.toSingleValueMap())
						.build()
		);

		return ResponseEntity
				.status(HttpStatus.OK.value())
				.body(drivingScoreVO.getDrivingScoreResDTO());
	}
}
