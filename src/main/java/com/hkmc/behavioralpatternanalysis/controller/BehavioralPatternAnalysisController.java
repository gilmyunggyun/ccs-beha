package com.hkmc.behavioralpatternanalysis.controller;

import com.hkmc.behavioralpatternanalysis.behavioral.model.UbiSafetyReqDTO;
import com.hkmc.behavioralpatternanalysis.behavioral.model.UbiSafetyResDTO;
import com.hkmc.behavioralpatternanalysis.behavioral.model.UbiSafetyVO;
import com.hkmc.behavioralpatternanalysis.behavioral.service.BehavioralPatternService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hkmc.behavioralpatternanalysis.common.Const;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "소비자 행동패턴 분석 서비스")
@RequestMapping(Const.BehavioralPatternAnalysis.VERSION_V1)
@RequiredArgsConstructor
public class BehavioralPatternAnalysisController {

	private final BehavioralPatternService behavioralPatternService;

	@ApiOperation(value = "UBI 안전 운전 점수 조회")
	@PostMapping(value="/ubiscore/{vinPath}")
	public ResponseEntity<UbiSafetyResDTO> ubiSafetyDrivingScore (@RequestHeader HttpHeaders headers,
																  @PathVariable("vinPath") String vinPath,
																  @RequestBody UbiSafetyReqDTO body) {
		return ResponseEntity.ok()
				.body(behavioralPatternService.ubiSafetyDrivingScore(
						UbiSafetyVO.builder()
								.vinPath(vinPath)
								.header(headers.toSingleValueMap())
								.ubiSafetyReq(body)
								.build()
				));
	}

}
