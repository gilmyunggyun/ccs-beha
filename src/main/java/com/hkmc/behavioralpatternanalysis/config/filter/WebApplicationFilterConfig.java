package com.hkmc.behavioralpatternanalysis.config.filter;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.filter.ValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;

@Configuration
public class WebApplicationFilterConfig {

	@Value("${validation.check}")
	public String validationCheck;
	
	@Bean
	public FilterRegistrationBean<Filter> validationFilter(){
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new ValidationFilter(validationCheck));
		filterRegistrationBean.setUrlPatterns(Arrays.asList(Const.BehavioralPatternAnalysis.VERSION_V1+"/*"));
		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}

}
