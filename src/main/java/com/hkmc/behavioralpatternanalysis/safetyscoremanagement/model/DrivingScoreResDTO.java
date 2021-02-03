package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DrivingScoreResDTO extends SpaResponseDTO {
    @JsonProperty("safetyDrivingScore")
    @ApiModelProperty(value = "", required = false, example = "")
    private Integer safetyDrivingScore;

    @JsonProperty("insuranceDiscountYN")
    @ApiModelProperty(value = "", required = false, example = "")
    private String insuranceDiscountYN;

    @JsonProperty("updateAt")
    @ApiModelProperty(value = "", required = false, example = "")
    private String updateAt;

    @JsonProperty("drvDistance")
    @ApiModelProperty(value = "", required = false, example = "")
    private Integer drvDistance;

    @JsonProperty("accelGrade")
    @ApiModelProperty(value = "", required = false, example = "")
    private String accelGrade;
    
    @ApiModelProperty(value = "", required = false, example = "")
    @JsonProperty("decelGrade")
    private String decelGrade;
    
    @ApiModelProperty(value = "", required = false, example = "")
    @JsonProperty("nightDrivingGrade")
    private String nightDrivingGrade;

    @Override
    public String toString() {
        return "DrivingScoreResDTO{" +
                "ServiceNo='" + this.getServiceNo() + '\'' +
                ", RetCode='" + this.getRetCode() + '\'' +
                ", svcTime=" + this.getSvcTime() +
                ", FncCnt=" + this.getFncCnt() +
                ", resCode='" + this.getResCode() + '\'' +
                ", safetyDrivingScore=" + safetyDrivingScore +
                ", insuranceDiscountYN='" + insuranceDiscountYN + '\'' +
                ", updateAt='" + updateAt + '\'' +
                ", drvDistance=" + drvDistance +
                ", accelGrade='" + accelGrade + '\'' +
                ", decelGrade='" + decelGrade + '\'' +
                ", nightDrivingGrade='" + nightDrivingGrade + '\'' +
                '}';
    }
}
