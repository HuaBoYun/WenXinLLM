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

@TableName("TBL_NBSJ_REFOPM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjRefopmEntity {
	public static final Integer STATUSYES=2; //整改完成
	public static final Integer STATUSNO=1;//整改中
	public static final Integer NUSTATUS=0;//未分派
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "reformid", type= IdType.AUTO)
	@Schema
	private BigDecimal reformid;
	
	@TableField(value = "reformmeasure")
	@Schema
	private String reformmeasure;
	
	@TableField(value = "reformresult")
	@Schema
	private String reformresult;
	
	@TableField(value = "reformcarryout")
	@Schema
	private String reformcarryout;
	
	@TableField(value = "handling")
	@Schema
	private String handling;
	
	@TableField(value = "reformdeadline")
	@Schema
	private Date reformdeadline;
	
	@TableField(value = "reformtime")
	@Schema
	private Date reformtime;
	
	@TableField(value = "status")
	@Schema
	private Integer status;
	
	@TableField(value = "tblBug")
	@Schema
	private TblNbsjBugEntity tblBug;
	
	@TableField(value = "nbsjQuestion")
	@Schema
	private TblNbsjQuestionEntity nbsjQuestion;
	
	@TableField(value = "nbsuser")
	@Schema
	private TblStaff nbsuser;
	
	@TableField(value = "tblCeaters")
	@Schema
	private TblStaff tblCeaters;
	
	@TableField(value = "createDate")
	@Schema
	private Date createDate;
	
	@TableField(value = "lastreformstatus")
	@Schema
	private Integer lastreformstatus;
	
	@TableField(value = "tblNbsjReformSolution")
	@Schema
	private TblNbsjReformSolutionEntity tblNbsjReformSolution;
	
	@TableField(value = "tblnbsjProject")
	@Schema
	private TblNbsjProject tblnbsjProject;
	
	@TableField(value = "nextmeasures")
	@Schema
	private String nextmeasures;
	
	@TableField(value = "nextmplancomdate")
	@Schema
	private Date nextmplancomdate;
	
	@TableField(value = "inspect")
	@Schema
	private String inspect;
	
	@TableField(value = "zgdwdate")
	@Schema
	private Date zgdwdate;
	
	@TableField(value = "zgstatus")
	@Schema
	private Integer zgstatus;
	
	@TableField(value = "closeyy")
	@Schema
	private String closeyy;
	
	@TableField(value = "tblNbsjReformAtts")
	@Schema
	private Set<TblAttachment> tblNbsjReformAtts;
	
	
}
