package com.hkmc.behavioralpatternanalysis.safetyscore.service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreVO;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreResDTO;

public interface SafetyScoreService {
	DrivingScoreResDTO ubiSafetyDrivingScoreRequest(DrivingScoreVO drivingScoreVO) throws GlobalExternalException;
}
