package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.ResponseDTO;

public interface SafetyScoreManagementService {

	public ResponseEntity<ResponseDTO<Map<String, Object>>> ubiSafetyDrivingScoreSearch (String inUri, Map<String, Object> bodyMap, Map<String, String> reqHeader) throws GlobalCCSException;
	
	public ResponseEntity<ResponseDTO<Map<String, Object>>> ubiSafetyDrivingScoreDelete (String inUri, Map<String, Object> bodyMap, Map<String, String> reqHeader) throws GlobalCCSException;
}
