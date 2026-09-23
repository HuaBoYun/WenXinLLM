package com.huabo.audit.oracle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 业务表单多对多关联组织或用户的中间关系表
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_RECTIFICATION_ISSUES")
@Schema(name="TblRectificationIssues对象", description="整改方案与整改清单中间关系表")
public class TblRectificationIssuesVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private String relaId;

    @Schema(name = "整改清单主键")
  private String issuesId;

    @Schema(name = "整改方案主键")
  private String planId;

    @Schema(name = "经办人")
  private BigDecimal handlerId;
   
    @Schema(name = "经办人名称")
  private String handlerName;

    @Schema(name = "整改方案")
  private String rectificationPlan;

    @Schema(name = "整改措施")
  private String rectificationMeasures;

    @Schema(name = "成果体现")
  private String resultMemo;

    @Schema(name = "预计完成时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
  private Date deadline;

    @Schema(name = "整改落实人")
  private BigDecimal implementer;

    @Schema(name = "整改落实人姓名")
  private String implementerName;
    
    @Schema(name = "版本信息")
  private Integer version;

    @Schema(name = "责任人")
  private BigDecimal responsiblePerson;
    
    @Schema(name = "责任人姓名")
    private String responsiblePersonName;

    @Schema(name = "责任部门")
  private BigDecimal responsibleDept;
    
    @Schema(name = "责任部门名称")
    private String responsibleDetpName;

    @Schema(name = "数据状态 0-未分派，1-审批中 ，2-已退回 3-已撤销  ,6-落实完成 ，7-已分派 ，8-开始落实")
  private Integer status;

    @Schema(name = "整改清单返回实体")
  private TblZgzzIssuesilistVo issues;
    
    @Schema(name = "整改方案返回实体")
  private TblZgzzRectificationplanVo plan;
    
    @Schema(name = "我的整改落实信息返回实体")
  private TblZgzzRectificationimplVo reimpl;
    
    @Schema(name = "整改评价信息返回实体")
  private TblZgzzRctevaluationVo valua;
    
    @Schema(name = "承办部门主键拼接字符串")
  private String cborgIds;
  
    @Schema(name = "承办部门名称拼接字符串")
  private String cborgNames;
    
    @Schema(name = "落实信息主键")
  private String implId;
    
    @Schema(name = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
    
    @Schema(name = "附件列表")
    @Transient
    @IgnoreSwaggerParameter
  private List<TblAttachment> attList = new ArrayList<TblAttachment>(0);
    
    @Schema(name = "整改情况概述")
    private String situationoverView;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

   @Schema(name = "知悉范围id")
   @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
}