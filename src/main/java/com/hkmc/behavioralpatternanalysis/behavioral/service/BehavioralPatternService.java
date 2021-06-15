package com.hkmc.behavioralpatternanalysis.behavioral.service;

import com.hkmc.behavioralpatternanalysis.behavioral.model.UbiSafetyResDTO;
import com.hkmc.behavioralpatternanalysis.behavioral.model.UbiSafetyVO;

public interface BehavioralPatternService {

    UbiSafetyResDTO ubiSafetyDrivingScore(UbiSafetyVO ubiSafetyVO);

}
