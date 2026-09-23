package com.huabo.fxgl.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.fxgl.entity.enums.LoseEventCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */

@Data
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_RISKEVENT")
public class Riskevent extends FlexibleFieldEntity implements Serializable {



    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal riseveid;

	@Schema(name="事件编号")
    private String riskeventcode;

	@Schema(name="事件名称")
    private String riskeventname;

	@Schema(name="事件描述")
    private String riskeventdescription;

	@Schema(name="间接损失")
    private String indirectloss;

	@Schema(name="直接损失")
    private String directloss;

	@Schema(name="经办人ID")
    private String riskfactor1;

	@Schema(name="经办人名称")
    private String riskfactor2;

	@Schema(name="备注")
    private String memo;

	  @Schema(name = "发生日期")
      @TableField("OCCUREDDATE")
      @JSONField(format = "yyyy-MM-dd")
	  @DateTimeFormat(pattern="yyyy-MM-dd")
	  @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
      private Date occureddate;
	
	@Schema(name="发生部门")
    private String occureddepartment;
	
	
	@Schema(name="发生部门id")
    private BigDecimal occureddepartmentid;

	@Schema(name="类别")
    private String losseventcategory;

   // private LoseEventCategory losseventcategory;

	@Schema(name="发现日期")
    @TableField("DISCOVEREDDATE")
    @JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date discovereddate;

	@Schema
    private BigDecimal inriskeventdb;

	@Schema
    private String indirectlossdes;

	@Schema
    private String directlossdes;

	@Schema
    private String unit;

	@Schema
    private String eventstatus;

	@Schema(name="经办部门ID")
    private String recordorg;

	@Schema(name="经办部门名称")
    private String recorddepart;

	@Schema
    private String subsystem;

	@Schema
    private String bussiness;

	@Schema(name="最大预估损失汇总金额")
    private BigDecimal maxestimateloss;

	@Schema(name="已确认的直接损失总金额")
    private BigDecimal confirmeddirectloss;

	@Schema(name="已确认的直接损失净额")
    private BigDecimal confirmeddirectlossa;

	@Schema
    private Integer riskcatid;

    public BigDecimal getRiseveid() { return riseveid; }
    
    
    
    @Schema(name="审批状态")
    private BigDecimal status;
    
    @Schema(name="富文本框")
    private String content;

    //中核新增字段    
    @Schema(name="涉及企业名称")
    private String involvingenterprisenames;
    
    @Schema(name="涉及企业层级 Involving enterprise hierarchy")
    private String involvingenterprisehierarchy;
    
    @Schema(name="当期情况描述")
    private String description;
    
    
    @Schema(name="损失(风险)金额(万元)")
    private BigDecimal amountofdamages;
    
    @Schema(name="处置进展情况")
    private String progressofdisposal;
    
    @Schema(name="父级版本关联id")
    private BigDecimal fatherriseveid;
    
    @Schema(name="父级版本编号")
    private String fatherriskeventcode;
    //同一个编号的风险事件在创建过程中 可能以子级中的版本来继续创建 这样就记录初始父级版本  
         //然后再记录子级  减少列表查询
    @Schema(name="初始父级版本关联id")
    private BigDecimal initialfatherriseveid;
    
    @Schema(name="初始父级版本编号")
    private String initialfatherriskeventcode;
    
    @Schema(name="上报状态  0未上报  1已上报")
    @TableField("REPORTSTATUS")
    private String  reportStatus;
    
    @Schema(name="版本 从1.0开始 自动增加")
    private String  version;
    
    @Schema(name = "创建日期")
    @TableField("CREATEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createdate;
    
    @Schema(name = "上报提交日期")
    @TableField("TOREPORTDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date toreportdate;
    
    @Schema(name = "查询日期条件起始")
    @TableField(exist=false)
    private String startDate;
    
    
    @Schema(name = "查询日期条件终止")
    @TableField(exist=false)
    private String endDate;
    
    @Schema(name = "风险类型名称")
    @TableField(exist=false)
    private String riskcatname;
   
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
    
    @Schema(name = "填报状态  0首报 1续报 2终报")
    @TableField("FILLINGSTATUS")
    private BigDecimal fillingStatus;
    
    @Schema(name="关联公司名称")
    @TableField(exist=false)
    private String unitname;
    
    
	  @Schema(name = "左侧树条件部门钻取下级节点")
	  @TableField(exist=false)
	  private List<BigDecimal> deptIds;
	  
	  @Schema(name = "左侧树条件公司钻取下级节点")
	  @TableField(exist=false)
	  private List<BigDecimal> unitIds;
    @Schema(name="导出的主键ids")
    @TableField(exist=false)
    private String ids;
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
 

}
