package com.hkmc.behavioralpatternanalysis.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "SPA 공통응답객체")
public class SpaResponseDTO {

	@JsonProperty("ServiceNo")
	@ApiModelProperty(value = "서비스 구분 코드", required = true, example = "F6")
	private String ServiceNo;

	@JsonProperty("RetCode")
	@ApiModelProperty(value = "S:제어요청접수 / F:제어요청실패", required = true, example = "S")
	private String RetCode;

	@ApiModelProperty(value = "센터에서 발급한 세션 ID", required = true)
	private int svcTime;

	@JsonProperty("FncCnt")
	@ApiModelProperty(value = "센터에서 발급한 서비스 세션 ID", required = true, example = "4")
	private int FncCnt;

	/**
	 * 0000	제어요청 정상처리
	 * E100	차량제어 서비스 중복실행
	 * E200	일별 차량제어요청 초과
	 * S999	전문 형식 에러
	 * CC01	차량 제어 권한 없음 (CCSP VEHICLE SERVER 4002에러)
	 * CC03	개인정보 설정변경 중
	 * CC04	개인정보 OFF 설정임
	 * CC05	원격대기시간 종료 상태
	 */
	@ApiModelProperty(value = "응답 메세지 코드", required = true, example = "0000")
	private String resCode;


}
