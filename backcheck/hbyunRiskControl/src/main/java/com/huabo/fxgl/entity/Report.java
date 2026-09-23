package com.huabo.fxgl.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ws.mime.Attachment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@Data
@TableName("TBL_REPORT")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Report extends  FlexibleFieldEntity implements Serializable {

    public  final static String OFF="OFF";//关闭
    public final static String ON="ON";//开启

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal reportid;

	@Schema(name="报告名称")
    private String reportname;

	@Schema
    private LocalDateTime reporttime;

	@Schema(name="报告类型")
    private String reporttype;

	@Schema(name="报告方式")
    private String reportmode;

	@Schema
    private String reporter;

	@Schema(name="报告部门")
    private String reportdepartment;

	@Schema
    private BigDecimal reporttempid;

	@Schema(name="复核状态")
    private String reportstatus;

	@Schema
    private String reportfile;

	@Schema
    private String memo;

	@Schema
    private String type;

	@Schema
    @TableField("REPDESC")
	private String repdesc;

	@Schema
    private BigDecimal projectid;

	@Schema
    private BigDecimal orgid;

	@Schema
    private String yjdes;

	@Schema
    private BigDecimal fhstaffid;

	@Schema
    private BigDecimal zqyjstaffid;

	@Schema
    private String reportcode;

	@Schema
    private LocalDateTime sendtime;

	@Schema(name="复核人")
    private String fhstaffname;

	@Schema
    private BigDecimal reporterid;

	@Schema
    private BigDecimal reportdepartmentid;

	@Schema(name="征求意见人")
    private String zqyjstaffname;

    //状态
	@Schema
    @TableField(exist = false)
    private Organization Organization;

    //状态
	@Schema
    @TableField(exist = false)
    private BigDecimal projectId;


//    //状态
//	@Schema
//    @TableField(exist = false)
//    private String status;

	@Schema
    @TableField(exist = false)
    private String templeinfo;

    public void setProjectId(BigDecimal projectId) {
        this.projectId = projectId;
    }

	@Schema
    @TableField(exist = false)
    private Reporttemple reporttemple;
	
	
	@Schema(name="报告层级")
    private String reportlevel;
	
	@Schema
    private BigDecimal status;

	 @Schema(name = "上报提交日期")
	    @TableField("TOREPORTDATE")
	    @JSONField(format = "yyyy-MM-dd")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
		@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	    private Date toreportdate;

	  @Schema(name="上报状态  0未上报  1已上报")
	    private String  reportsubstatus;
	    
	  
	  
	  //密级及查询条件
	    @Schema(name = "密级主键")
	    @TableField("SECRECTLEVELID")
	    private BigDecimal secrectLevelId;
	    
	    @Schema(name = "知悉范围id")
	    @TableField("STAFFSCOPEIDS")
	    private String staffScopeIds;
	    
	    @Schema(name = "知悉范围名称")
	    @TableField("STAFFSCOPENAMES")
	    private String staffScopeNames;
	 
	    @Schema(name = "所属部门")
	    @TableField("LINKDEPTID")
	  private BigDecimal linkdeptid;
	    
	    @Schema(name="创建人")
	    @TableField("CREATESTAFFID")
	    private BigDecimal createstaffid;
	    
	    @TableField("CREATETIME")
	 	@Schema(name = "创建时间")
	     private Date createtime;

	    @Schema(name = "所属公司名称")
	    @TableField(exist=false)
	    private String linkorgname;
}
