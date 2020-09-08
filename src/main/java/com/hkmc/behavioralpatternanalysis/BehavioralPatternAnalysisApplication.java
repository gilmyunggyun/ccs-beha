package com.hkmc.behavioralpatternanalysis;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

import ccs.core.db.SpringApplicationContextInitializer;

@SpringBootApplication
@EnableFeignClients
public class BehavioralPatternAnalysisApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BehavioralPatternAnalysisApplication.class)
        .initializers(new SpringApplicationContextInitializer())
        .application()
        .run(args);
	}

}
