package com.hkmc.behavioralpatternanalysis;

import ccs.core.data.encrypt.EnablePropertyEncrypt;
import com.hkmc.transactionlogger.EnableCcsp20TransactionLogger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

import ccs.core.db.SpringApplicationContextInitializer;

@SpringBootApplication
@EnableFeignClients
@EnableCcsp20TransactionLogger
@EnablePropertyEncrypt
public class BehavioralPatternAnalysisApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(BehavioralPatternAnalysisApplication.class)
        .initializers(new SpringApplicationContextInitializer())
        .application()
        .run(args);
	}

}
