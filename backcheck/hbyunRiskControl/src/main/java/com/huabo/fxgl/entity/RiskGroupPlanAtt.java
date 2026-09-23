package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

 
@Data
@ToString
@TableName("TBL_RISK_GROUPPLAN_ATT")
@AllArgsConstructor
@NoArgsConstructor
public class RiskGroupPlanAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal id;

    private BigDecimal attid;


}
