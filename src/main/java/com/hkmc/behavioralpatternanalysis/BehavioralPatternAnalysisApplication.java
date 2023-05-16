package com.hkmc.behavioralpatternanalysis;

import ccs.core.data.encrypt.EnablePropertyEncrypt;
import com.hkmc.filter.EnableTransactionLogger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableFeignClients
@EnablePropertyEncrypt
@EnableTransactionLogger
public class BehavioralPatternAnalysisApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BehavioralPatternAnalysisApplication.class)
        .application()
        .run(args);
	}

}
