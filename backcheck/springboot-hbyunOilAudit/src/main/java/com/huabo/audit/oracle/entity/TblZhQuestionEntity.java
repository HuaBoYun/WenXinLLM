package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_ZH_QUESTION")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblZhQuestionEntity {
	
	@TableId(value = "qustid", type= IdType.AUTO)
	@Schema
	private BigDecimal qustid;
	
	@TableField(value = "rulename")
	@Schema
	private String quescode;
	
	@TableField(value = "rulename")
	@Schema
	private TblNbsjProject tblpro;
	
	@TableField(value = "rulename")
	@Schema
	private TblReportEntity tblreport;
	
	@TableField(value = "rulename")
	@Schema
	private String sheetCode;
	
	@TableField(value = "rulename")
	@Schema
	private String quesTitle;
	
	@TableField(value = "rulename")
	@Schema
	private String riskTips;
	
	@TableField(value = "rulename")
	@Schema
	private String riskattention;
	
	@TableField(value = "rulename")
	@Schema
	private String risklevel;
	
	@TableField(value = "rulename")
	@Schema
	private TblOrganization organ;
	
	@TableField(value = "rulename")
	@Schema
	private TblOrganization branch;
	
	@TableField(value = "rulename")
	@Schema
	private String channel;
	
	@TableField(value = "rulename")
	@Schema
	private String wheReform;
	
	@TableField(value = "rulename")
	@Schema
	private Date createDate;
	
	@TableField(value = "rulename")
	@Schema
	private TblStaff createUser;
}
