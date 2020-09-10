package com.hkmc.behavioralpatternanalysis.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration(exclude = {R2dbcAutoConfiguration.class})
class BehavioralPatternAnalysisControllerTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
