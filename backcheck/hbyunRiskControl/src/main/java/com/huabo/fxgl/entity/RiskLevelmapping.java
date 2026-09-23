package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("TBL_RISK_LEVELMAPPING")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
//风险评估登记
public class RiskLevelmapping implements Serializable {

	//[{"rlevelmapid": "","poss1": "1","poss2": "2","poss3": "3","poss4": "4","poss5": "5"},{"rlevelmapid": "","poss1": "1","poss2": "2","poss3": "3","poss4": "4","poss5": "5"},{"rlevelmapid": "","poss1": "1","poss2": "2","poss3": "3","poss4": "4","poss5": "5"},{"rlevelmapid": "","poss1": "1","poss2": "2","poss3": "3","poss4": "4","poss5": "5"},{"rlevelmapid": "","poss1": "1","poss2": "2","poss3": "3","poss4": "4","poss5": "5"}]
	
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    private BigDecimal rlevelmapid;

    private String poss1; //0-未设置  1-极低 2-较低 3-中等 4-较高 5-极高

    private String poss2;

    private String poss3;

    private String poss4;

    private String poss5;

    private String memo;

    private BigDecimal assstdid;

    private String infludegree;

    @TableField(exist = false)
    private Integer count1 = 0;
    @TableField(exist = false)
    private Integer count2= 0;
    @TableField(exist = false)
    private Integer count3= 0;
    @TableField(exist = false)
    private Integer count4= 0;
    @TableField(exist = false)
    private Integer count5= 0;
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
	public RiskLevelmapping(RiskLevelmapping r) {
		super();
		this.poss1 = r.poss1;
		this.poss2 = r.poss2;
		this.poss3 = r.poss3;
		this.poss4 = r.poss4;
		this.poss5 = r.poss5;
		this.memo = r.memo;
		this.count1 = r.count1;
		this.count2 = r.count2;
		this.count3 = r.count3;
		this.count4 = r.count4;
		this.count5 = r.count5;
		this.risk1Ids = r.risk1Ids;
		this.risk2Ids = r.risk2Ids;
		this.risk3Ids = r.risk3Ids;
		this.risk4Ids = r.risk4Ids;
		this.risk5Ids = r.risk5Ids;
	}

	
	public RiskLevelmapping() {
		
	}

    
    
}
