package com.hkmc.behavioralpatternanalysis.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.model.CommonMessage;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
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
		return basicConfig("Vehicle Status service", Const.BehavioralPatternAnalysis.VERSION_V1);
	}

	public Docket basicConfig(String groupName, String paths) {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName(groupName)
				.apiInfo(
						new ApiInfoBuilder()
								.title("CCSP 2.0 API 서비스 " + "[" + groupName + "]")
								.version("1.0.0")
								.description("CCSP 2.0 " + groupName + " Document 입니다.")
								.build()
				)
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, responseMessage())
				.globalResponseMessage(RequestMethod.POST, responseMessage())
				.globalResponseMessage(RequestMethod.PATCH, responseMessage())
				.globalResponseMessage(RequestMethod.PUT, responseMessage())
				.select()
				.apis(RequestHandlerSelectors.basePackage(basePackage))
				.paths(PathSelectors.ant(paths + "/**"))
				.build();
	}

	private ArrayList<ResponseMessage> responseMessage() {
		return new ArrayList<>(Arrays.asList(
				new ResponseMessageBuilder().code(200).message(CommonMessage.SWAGGER_COMMON_200_MESSAGE).build()
				, new ResponseMessageBuilder().code(500).message(CommonMessage.SWAGGER_COMMON_500_MESSAGE).build()
		));
	}

}
