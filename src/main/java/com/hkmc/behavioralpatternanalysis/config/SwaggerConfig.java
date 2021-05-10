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
		return this.basicConfig("Behavioral Pattern Analysis service", Const.BehavioralPatternAnalysis.BASE_URL);
	}

	public Docket basicConfig(final String groupName, final String paths) {
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
				.globalOperationParameters(getHeaderParmeter())
				.globalResponseMessage(RequestMethod.GET, this.responseMessage())
				.globalResponseMessage(RequestMethod.POST, this.responseMessage())
				.globalResponseMessage(RequestMethod.PATCH, this.responseMessage())
				.globalResponseMessage(RequestMethod.PUT, this.responseMessage())
				.select()
				.apis(RequestHandlerSelectors.basePackage(basePackage))
				.paths(PathSelectors.ant(paths + "/**"))
				.build();
	}

	private ArrayList<ResponseMessage> responseMessage() {
		return new ArrayList<>(Arrays.asList(
				new ResponseMessageBuilder().code(200).message(CommonMessage.SWAGGER_COMMON_200_MESSAGE).build()
				,new ResponseMessageBuilder().code(500).message(CommonMessage.SWAGGER_COMMON_500_MESSAGE).build()
		));
	}

	private List<Parameter> getHeaderParmeter(){
		List<Parameter> headerList = new ArrayList<>();
		for(HEADERS header : Arrays.asList(HEADERS.values())) {
			headerList.add(new ParameterBuilder().name(header.key)
					.description(header.description)
					.required(header.required)
					.defaultValue(header.defaultValue)
					.modelRef(new ModelRef("string"))
					.parameterType("header")
					.build());
		}

		return headerList;
	}

	public enum HEADERS {
		/** 공통 영역 **/
		NADID("nadid", "01068404822", "단말기(TMU) 번호", true)
		,VIN("vin", "KMLFTEST014042301", "차대번호", true)
		,FROM("from", "PHONE", "요청지 HOST", true)
//		,TO("to", "ISS", "목적지 HOST", true)
//		,LANGUAGE("language", "3", "언어 (국내는 Default:3) 0 – English 1 – French 2 – Spanish 3 – Korean 4 – Chinese 5 – Portuguese 6 – Pekingese(중국 북경어) 7 – Cantonese(중국 광둥어)", true)
//		,OFFSET("offset", "9", "Offset value to convert UTC to Local time zone.", true)
		,CONTENTTYPE("Content-Type", "application/json", "Content-Type", true)


		/** 서비스별 Custom **/
		,APPMODE("appMode", "GEN2", "appMode", false)
		,XTID("xtid", "1122334455", "MSA  식별값", false)
		,BRANDCD("brandCd", "BL", "어플리케이션 사용 식별 brand (BL, UV, GE, BC, UC)", false)
		;

		HEADERS(String key, String defaultValue, String description, Boolean required) {
			this.key = key;
			this.defaultValue = defaultValue;
			this.description = description;
			this.required = required;
		}
		public String key;
		public String defaultValue;
		public String description;
		public Boolean required;
	}

}
