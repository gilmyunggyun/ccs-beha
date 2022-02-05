package com.hkmc.behavioralpatternanalysis;

import com.hkmc.annotation.ConnectedCarApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

@ConnectedCarApplication
@EnableFeignClients
public class BehavioralPatternAnalysisApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BehavioralPatternAnalysisApplication.class)
        .application()
        .run(args);
	}

}
