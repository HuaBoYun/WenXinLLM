package com.huabo.fxgl.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("TBL_RISK_COPING")
@Schema(name="TBL_RISK_COPING", description="风险应对")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class RiskCoping extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal riskcopingid;

	@Schema(name="风险应对策略类型")
    private String copingplot;

	@Schema(name="应对状态")
    private String copingstatus;

	@Schema(name="风险应对负责人ID")
    private String copinghead;
	
	@Schema(name="风险应对负责人名称")
	@TableField(exist=false)
    private String copingheadname;

	@Schema
    private String copingdes;

	@Schema(name="关联风险ID")
    private BigDecimal riskid;

	@Schema
    private String copingsource;

	@Schema(name="风险期望值") 
    private Integer riskhopevalue;

	@Schema(name="应对方案")
    private String yddes;

	@Schema
    private BigDecimal status;

	@Schema(name="一级复核人")
    private String reviewer;

	@Schema(name="二级复核人")
    private String reviewer2;
	
	@Schema(name="富文本框")
    private String content;
	
	//==一体化运行评价
	@Schema(name="评价标准及要点")
    private String evalimp;
	
	@Schema(name="文档")
    private String evalfile;

	
	 @Schema(name = "创建日期")
	    @TableField("CREATEDATE")
	    @JSONField(format = "yyyy-MM-dd")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
		@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	    private Date createdate;
	 
		@Schema(name="归属公司id")
		@TableField(value="UNIT")
	    private BigDecimal unit;
		
		@Schema(name="归属公司名称")
		@TableField(exist=false)
		private String unitname;

}
