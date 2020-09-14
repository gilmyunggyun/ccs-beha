package com.hkmc.behavioralpatternanalysis.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.model.CommonMessage;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***************************************************
 * 설     명 : swagger 사용을 위한 기본 설정 선언
 ***************************************************/
@Configuration
@EnableSwagger2
@Profile(value = {"local","dev"})
public class SwaggerConfig {
	
	private static String basePackage = "com.hkmc";
	
	@Bean
	public Docket serviceApi() {
		return this.basicConfig("TEMPLATE service", Const.BehavioralPatternAnalysis.VERSION_V1);
	}

	public Docket basicConfig(final String groupName, final String paths) {
		 return new Docket(DocumentationType.SWAGGER_2)
				 .groupName(groupName)
				 .apiInfo(
					  new ApiInfoBuilder()
					  	.title(new StringBuilder("CCSP 2.0 API 서비스 ").append("[").append(groupName).append("]").toString())
				        .version("1.0.0")
				        .description(new StringBuilder("CCSP 2.0 ").append(groupName).append(" Document 입니다.").toString())
				        .build()
				 )
				 .useDefaultResponseMessages(false)
				 .globalResponseMessage(RequestMethod.GET, this.responseMessage())
				 .globalResponseMessage(RequestMethod.POST, this.responseMessage())
				 .globalResponseMessage(RequestMethod.PATCH, this.responseMessage())
				 .globalResponseMessage(RequestMethod.PUT, this.responseMessage())
				 .select()
				 .apis(RequestHandlerSelectors.basePackage(basePackage))				 
				 .paths(PathSelectors.ant(new StringBuilder(paths).append("/**").toString()))
				 .build();
	}
		
    private ArrayList<ResponseMessage> responseMessage() {
    	return new ArrayList<>(Arrays.asList(
    			new ResponseMessageBuilder().code(200).message(CommonMessage.SWAGGER_COMMON_200_MESSAGE).build()
    			,new ResponseMessageBuilder().code(500).message(CommonMessage.SWAGGER_COMMON_500_MESSAGE).build()
    			));
        
    }

}
