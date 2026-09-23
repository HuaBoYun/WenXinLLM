package com.huabo.fxgl.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

 
@Data
@TableName("TBL_MAJOR_TRANSFER")
@Schema(name="重大风险填报转派记录")
public class TblMajorTransfer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Schema(name = "主键")
	@TableId(value = "ID")
	@Id
	private BigDecimal id;

	@Schema(name = "转派人id")
	@TableField(value = "INITIALSTAFFID")
	private BigDecimal initialStaffid;

	@Schema(name = "转派人NAME")
	@TableField(value = "INITIALNAME")
	private String initialName;

	@Schema(name = "被转派人id")
	@TableField(value = "TOSTAFFID")
	private BigDecimal toStaffid;

	@Schema(name = "被转派人NAME")
	@TableField(value = "TONAME")
	private String toName;

	@Schema(name = "关联重大风险填报季度id")
	@TableField(value = "MAJORID")
	private BigDecimal majorId;
	
	@Schema(name = "关联重大风险填报风险id")
	@TableField(value = "IMPLEMENTID")
	private BigDecimal implementId;

	@Schema(name = "创建时间")
	@TableField(value = "CREATETIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createtime;
	
	 @TableField(value = "IMP_RISK_NAME")
	 @Schema(name="风险名称")
	 private String impRiskName;

}

