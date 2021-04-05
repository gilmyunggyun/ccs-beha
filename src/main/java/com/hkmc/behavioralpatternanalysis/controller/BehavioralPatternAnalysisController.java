package com.hkmc.behavioralpatternanalysis.controller;

import com.hkmc.behavioralpatternanalysis.intelligence.model.ItlBreakpadReqDTO;
import com.hkmc.behavioralpatternanalysis.intelligence.model.ItlBreakpadResDTO;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreReqDTO;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreVO;
import com.hkmc.behavioralpatternanalysis.safetyscore.service.SafetyScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.intelligence.service.IntelligenceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "소비자 행동패턴 분석 서비스")
@RequestMapping(Const.BehavioralPatternAnalysis.VERSION_V1)
@RequiredArgsConstructor
public class BehavioralPatternAnalysisController {
	private final IntelligenceService intelligenceService;
	private final SafetyScoreService safetyScoreService;

	@ApiOperation(value = "차량 브레 이크 패드 자료에 대한 조회 요청을 처리")
	@PostMapping(value="/breakpad")
	public ResponseEntity<ItlBreakpadResDTO> getItlCarBreakpadDrvScore(@RequestHeader HttpHeaders header,
																	   @RequestBody ItlBreakpadReqDTO body) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.intelligenceService.getItlCarBreakpadDrvScore(body.getVin()));
	}

	@ApiOperation(value = "UBI 안전 운전 점수 조회")
	@PostMapping(value="/ubiscore/{vinPath}")
	public ResponseEntity<?> getUbiSafetyDrivingScore (@RequestHeader HttpHeaders headers,
													   @PathVariable("vinPath") String vinPath,
													   @RequestBody DrivingScoreReqDTO body) {
		return ResponseEntity
				.status(HttpStatus.OK.value())
				.body(this.safetyScoreService.ubiSafetyDrivingScoreRequest(
						DrivingScoreVO.builder()
								.drivingScoreReqDTO(body)
								.vinPath(vinPath)
								.header(headers.toSingleValueMap())
								.build()
				));
	}
}
