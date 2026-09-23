package com.huabo.monitor.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblTestElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal elementId;
    private String elementCode;//编号
    private String riskType; //风险描述
    private String checkMethod; //检查方法
    private String controlMethod;//控制方法
    private String controlType;//控制类型
    private String controlReq;//控制频率
    private String material;//检查材料
    private String businessDesc;//业务描述
    private String controlTarget;//控制目标
    private String  controlMeasures;//控制措施
    private Date createTime;
    private BigDecimal typeId;
    private BigDecimal templId;
}
