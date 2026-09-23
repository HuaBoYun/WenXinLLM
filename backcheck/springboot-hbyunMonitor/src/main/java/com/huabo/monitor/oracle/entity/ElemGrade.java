package com.huabo.monitor.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElemGrade {
    private BigDecimal assStaffId;//TBL_ASSESS_STAFF的id
    private String elementName;//要素名称
    private Integer standardscore;//标准分
    private Integer score;//评价分
    private String reason;//理由
    private String attname;//附件名称
    private BigDecimal attid;
    private String examination;//审查要点.对应要素表中AUDITPOINT
    private String asseleid;//要素id
    private String elementNumber;//要素编号
}