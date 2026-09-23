package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@ToString
@TableName("TBL_RISK_INFLUDEGREE")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
//风险影响程度
public class RiskInfludegree implements Serializable {

    private static final long serialVersionUID = 1L;

    //[{"degreeid": "","rilevel": "1","infludegreedes": "说明1"},{"degreeid": "","rilevel": "2","infludegreedes": "说明2"},{"degreeid": "","rilevel": "3","infludegreedes": "说明3"},{"degreeid": "","rilevel": "4","infludegreedes": "说明4"},{"degreeid": "","rilevel": "5","infludegreedes": "说明5"}]

    //主键
    @TableId(type = IdType.INPUT)
    private BigDecimal degreeid;

    //风险评估标准
    @TableField(exist = false)
    private RiskAssessmentstd assessmentstd;

    //评估标准ID
    private BigDecimal assstdid;

    //风险等级
    private BigDecimal rilevel;

    //分类
    private String infludegreedes;

    private String memo;
    
    @TableField(exist = false)
    private RiskLevelmapping riskLevelMapping;

    @TableField(exist = false)
    private String risk1Ids = "";
    @TableField(exist = false)
    private String risk2Ids = "";
    @TableField(exist = false)
    private String risk3Ids = "";
    @TableField(exist = false)
    private String risk4Ids = "";
    @TableField(exist = false)
    private String risk5Ids = "";
    
    
	public RiskInfludegree(RiskInfludegree r) {
		super();
		this.rilevel = r.rilevel;
		this.infludegreedes = r.infludegreedes;
		this.memo = r.memo;
		this.risk1Ids = r.risk1Ids;
		this.risk2Ids = r.risk2Ids;
		this.risk3Ids = r.risk3Ids;
		this.risk4Ids = r.risk4Ids;
		this.risk5Ids = r.risk5Ids;
	}


	public RiskInfludegree() {
	}

}
