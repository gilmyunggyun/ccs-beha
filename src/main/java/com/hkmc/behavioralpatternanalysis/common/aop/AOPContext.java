package com.hkmc.behavioralpatternanalysis.common.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hkmc.behavioralpatternanalysis.common.aop.log.LogAspect;

@Configuration
public class AOPContext {
	
	@Bean(name = "logAspect")
	public LogAspect logAspect() {
		return new LogAspect();
	}

}
