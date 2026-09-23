package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@ToString
@TableName(value = "TBL_RISK_RISKMARKING",resultMap = "RM_RISK_RISK_MARKING")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class RiskRiskmarking implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评估人
     */
    @Schema(name="评估人")
    @TableField(value = "STAFFID", property = "staff.staffid")
    private Staff staff;

    /**
     * ID
     */
	@Schema(name="ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal markingid;

    /**
     * 发生频率
     */
	@Schema(name="发生频率")
    private Integer frequency;

    /**
     * 严重程度
     */
	@Schema(name="严重程度")
    private Integer severity;

    /**
     * 风险等级
     */
	@Schema(name="风险等级")
    private String risklevel;

    /**
     * 权重
     */
	@Schema(name="权重")
    private Double assweight;

    /**
     * 评估时间
     */
	@Schema(name="评估时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime assdate;

    /**
     * 计划和风险中间表ID
     */
	@Schema(name="计划和风险中间表ID")
    @TableField(value = "ASSRISKID", property = "riskAssplanRisk.assriskid")
    private RiskAssplanRisk riskAssplanRisk;

	@Schema
    private BigDecimal assriskid;
    /**
     * 评估内容
     */
	@Schema(name="评估内容")
    private String asscontent;

    /**
     * 状态
     */
	@Schema(name="状态")
    private BigDecimal asssatus;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;
	
	@Schema(name="评估人真实姓名")
    @TableField(exist = false)
	private String realname;
	
	@Schema(name="评估人评估任务数量")
    @TableField(exist = false)
	private Integer pgcount;
	//------------------------------------------------额外取的字段用于铺设前端取值
	@Schema(name="RISKID")
    @TableField(exist = false)
	private BigDecimal riskid;
	
	@Schema(name="RISKNUMBER")
    @TableField(exist = false)
	private String  risknumber;
	
	@Schema(name="RISKDES")
    @TableField(exist = false)
	private String riskdes;
	
	@Schema(name="RISKNAME")
    @TableField(exist = false)
	private String  riskname;
	

}
