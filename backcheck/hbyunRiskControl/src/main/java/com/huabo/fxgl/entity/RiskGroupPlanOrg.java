package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

 
@Data
@ToString
@TableName("TBL_RISK_GROUPPLAN_ORG")
public class RiskGroupPlanOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal orgid;

    private BigDecimal id;

}
