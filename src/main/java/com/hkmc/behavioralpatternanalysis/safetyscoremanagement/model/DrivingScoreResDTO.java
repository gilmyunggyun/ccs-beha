package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DrivingScoreResDTO extends SpaResponseDTO {
    @JsonProperty("safetyDrivingScore")
    @ApiModelProperty(value = "", required = false, example = "")
    @Default
    private Integer safetyDrivingScore = BigInteger.ZERO.intValue();

    @JsonProperty("insuranceDiscountYN")
    @ApiModelProperty(value = "", required = false, example = "")
    @Default
    private String insuranceDiscountYN = StringUtils.EMPTY;

    @JsonProperty("updateAt")
    @ApiModelProperty(value = "", required = false, example = "")
    @Default
    private String updateAt = StringUtils.EMPTY;

    @JsonProperty("drvDistance")
    @ApiModelProperty(value = "", required = false, example = "")
    @Default
    private Integer drvDistance = BigInteger.ZERO.intValue();

    @JsonProperty("accelGrade")
    @ApiModelProperty(value = "", required = false, example = "")
    @Default
    private String accelGrade = StringUtils.EMPTY;
    
    @ApiModelProperty(value = "", required = false, example = "")
    @Default
    @JsonProperty("decelGrade")
    private String decelGrade = StringUtils.EMPTY;
    
    @ApiModelProperty(value = "", required = false, example = "")
    @Default
    @JsonProperty("nightDrivingGrade")
    private String nightDrivingGrade = StringUtils.EMPTY;

    public DrivingScoreResDTO (Map<String, Object> body, String serviceNo){
        this.setServiceNo(serviceNo);
        this.setRetCode(SpaResponseCodeEnum.SUCCESS.getRetCode());
        this.setResCode(SpaResponseCodeEnum.SUCCESS.getResCode());
        this.safetyDrivingScore = Integer.parseInt(String.valueOf(
                Optional.ofNullable(body.get(Const.Key.SAFETY_DRV_SCORE)).orElse(BigInteger.ZERO.intValue())
        ));
        this.insuranceDiscountYN = String.valueOf(
                Optional.ofNullable(body.get(Const.Key.INS_DISCOUNT_YN)).orElse(StringUtils.EMPTY)
        );
        this.updateAt = String.valueOf(
                Optional.ofNullable(body.get(Const.Key.SCORE_DATE)).orElse(StringUtils.EMPTY)
        );
        this.drvDistance = Integer.parseInt(String.valueOf(
                Optional.ofNullable(body.get(Const.Key.RANGE_DRV_DIST)).orElse(BigInteger.ZERO.intValue())
        ));
        this.accelGrade = String.valueOf(
                Optional.ofNullable(body.get(Const.Key.BRST_ACC_GRADE)).orElse(StringUtils.EMPTY)
        );
        this.decelGrade = String.valueOf(
                Optional.ofNullable(body.get(Const.Key.BRST_DEC_GRADE)).orElse(StringUtils.EMPTY)
        );
        this.nightDrivingGrade = String.valueOf(
                Optional.ofNullable(body.get(Const.Key.NIGHT_DRV_GRADE)).orElse(StringUtils.EMPTY)
        );
    }
}
