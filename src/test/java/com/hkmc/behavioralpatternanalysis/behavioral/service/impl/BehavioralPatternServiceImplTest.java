package com.hkmc.behavioralpatternanalysis.behavioral.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class BehavioralPatternServiceImplTest {

    @Spy
    @InjectMocks
    private BehavioralPatternServiceImpl behavioralPatternService;

    @Test
    @DisplayName("안전운전 점수 조회")
    void testUbiSafetyDrivingScore() {

    }

}