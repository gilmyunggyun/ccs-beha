package com.hkmc.behavioralpatternanalysis;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.hkmc.filter.EnableTransactionLogger;
import ccs.core.data.encrypt.EnablePropertyEncrypt;

@SpringBootApplication
@EnableFeignClients
@EnableTransactionLogger
@EnablePropertyEncrypt
public class BehavioralPatternAnalysisApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BehavioralPatternAnalysisApplication.class)
        //.initializers(new SpringApplicationContextInitializer())
        .application()
        .run(args);
	}

}
