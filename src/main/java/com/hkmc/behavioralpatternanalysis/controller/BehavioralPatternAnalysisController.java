package com.hkmc.behavioralpatternanalysis.controller;

import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model.ItlBreakpadReqDTO;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model.ItlBreakpadResDTO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreReqDTO;
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
	@PostMapping(value="/itl/breakpad") //itlCarBreakpadDrvScore
	public ResponseEntity<ResponseDTO<ItlBreakpadResDTO>> getItlCarBreakpadDrvScore(
			@RequestHeader HttpHeaders header,
			@RequestBody ItlBreakpadReqDTO body
	) throws GlobalCCSException {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResponseDTO.<ItlBreakpadResDTO>builder()
						.code(HttpStatus.OK.value())
						.data(intelligenceVehicleInformationService.getItlCarBreakpadDrvScore(body.getVin()))
						.resultMessage(Const.ResponseMessage.SUCCESS).build());
	}

	@ApiOperation(value = "UBI 안전 운전 점수 조회")
	@PostMapping(value="/ubi/score") //ubiSafetyDrvScoreService
	public ResponseEntity<?> getUbiSafetyDrivingScore (
			@RequestHeader HttpHeaders headers,
			@RequestBody DrivingScoreReqDTO body
	) {
		return ResponseEntity
				.status(HttpStatus.OK.value())
				.body(safetyScoreManagementService.ubiSafetyDrivingScoreRequest(
						DrivingScoreVO.builder()
								.drivingScoreReqDTO(body)
								.vinPath(body.getVin())
								.header(headers.toSingleValueMap())
								.build()
				));
	}
}
