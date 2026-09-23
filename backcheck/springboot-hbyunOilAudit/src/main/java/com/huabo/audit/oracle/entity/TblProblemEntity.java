package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_PROBLEM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblProblemEntity {
	
	@TableId(value = "problemid", type= IdType.AUTO)
	@Schema
	private BigDecimal problemid;
	
	@TableField(value = "formName")
	@Schema
	private String probname;
	
	@TableField(value = "formName")
	@Schema
	private String probdescribe;
	
	@TableField(value = "formName")
	@Schema
	private String bussinessbelongsto;
	
	@TableField(value = "formName")
	@Schema
	private String riskbelongsto;
	
	@TableField(value = "formName")
	@Schema
	private String probfrom;
	
	@TableField(value = "formName")
	@Schema
	private String discoveryperson;
	
	
	@TableField(value = "formName")
	@Schema
	private Date discovertime;
	
	@TableField(value = "formName")
	@Schema
	private Date occuringtime;
	
	@TableField(value = "formName")
	@Schema
	private String proborgs;
	
	@TableField(value = "formName")
	@Schema
	private String needreform;
	
	@TableField(value = "formName")
	@Schema
	private String memo;
	
	@TableField(value = "formName")
	@Schema
	private String explainitem;
	
	@TableField(value = "formName")
	@Schema
	private String personincharge;
	
	@TableField(value = "formName")
	@Schema
	private String probnumber;
	
	@TableField(value = "formName")
	@Schema
	private String problembysystem;
	
	@TableField(value = "formName")
	@Schema
	private Set tblWorksheets;
	
	@TableField(value = "formName")
	@Schema
	private Set tblAttachments;
	
	@TableField(value = "formName")
	@Schema
	private Set tblReforms;
	
	@TableField(value = "formName")
	@Schema
	private Set<TblAssessTarget> tblproblemTargets;
	
	@TableField(value = "formName")
	@Schema
	private Integer inProblemdb;
	
	@TableField(value = "formName")
	@Schema
	private Integer probepartment;
	
}
