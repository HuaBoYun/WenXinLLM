package com.huabo.compliance.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TblFlowVo {

    private BigDecimal flowid;
    private String fatherflowid;
    private String company;
    private String flownumber;
    private String flowName;
    private String isFlowdb = "";
    private String departincharge;
    private String fathername;

}
