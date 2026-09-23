package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SXFXBD_RISKCLASS")
@Schema(name="RiskClassMySql")
public class RiskClassMySql {

    @TableId("RISKID")
    private BigDecimal riskid;
    @TableField("RISKNUMBER")//风险编号
    private String riskNumber;
    @TableField("RISKLEVEL")
    private String risklevel;//1-一类，2-二类，其他-三类
    @TableField("PARENTID")
    private String parentid;
    @TableField("POLICYBASIS")//政策依据
    private String policybasis;
    @TableField("RISKCLASS")//一类
    private String riskclass;
    @TableField("ORGID")
    private String orgid;
    private List<RiskClassMySql> childrenList = new ArrayList(0);
}
