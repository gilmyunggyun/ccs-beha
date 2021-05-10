package com.hkmc.behavioralpatternanalysis.behavioral.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "안전운전점수 응답 객체")
public class UbiSafetyResDTO extends SpaResponseDTO {

    @JsonProperty("safetyDrivingScore")
    @ApiModelProperty(value = "", required = false, example = "")
    @Builder.Default
    private Integer safetyDrivingScore = BigInteger.ZERO.intValue();

    @JsonProperty("insuranceDiscountYN")
    @ApiModelProperty(value = "", required = false, example = "")
    @Builder.Default
    private String insuranceDiscountYN = StringUtils.EMPTY;

    @JsonProperty("updateAt")
    @ApiModelProperty(value = "", required = false, example = "")
    @Builder.Default
    private String updateAt = StringUtils.EMPTY;

    @JsonProperty("drvDistance")
    @ApiModelProperty(value = "", required = false, example = "")
    @Builder.Default
    private Integer drvDistance = BigInteger.ZERO.intValue();

    @JsonProperty("accelGrade")
    @ApiModelProperty(value = "", required = false, example = "")
    @Builder.Default
    private String accelGrade = StringUtils.EMPTY;

    @ApiModelProperty(value = "", required = false, example = "")
    @Builder.Default
    @JsonProperty("decelGrade")
    private String decelGrade = StringUtils.EMPTY;

    @ApiModelProperty(value = "", required = false, example = "")
    @Builder.Default
    @JsonProperty("nightDrivingGrade")
    private String nightDrivingGrade = StringUtils.EMPTY;

}
