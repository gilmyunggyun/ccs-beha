package com.hkmc.behavioralpatternanalysis.behavioral.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Getter@Setter@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("BEHA_SVDV_HIST")
public class BehaSvdvHist implements Serializable {

    @Id
    @Column("CRTN_YMD")
    private String ifDate;

    @Column("NNID_VIN")
    private String nnidVin;

    @Column("SALE_CAML_CD")
    private String prjVehlCd;

    @Column("GOOD_SEVERE_SCTN")
    private String severeNormal;

    @Column("GOOD_DRIV_CNT")
    private int cntNormal;

    @Column("CAUTN_DRIV_CNT")
    private int cntCaution;

    @Column("SEVERE_DRIV_CNT")
    private int cntSevere;

    @Column("SEVERE_DRIV_PLUS_CNT")
    private int cntSeverePlus;

    @Column("CAR_OID")
    private int carOid;

}
