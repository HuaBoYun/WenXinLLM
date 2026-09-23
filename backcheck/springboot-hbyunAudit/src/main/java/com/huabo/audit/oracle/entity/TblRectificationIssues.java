package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@Table(name = "TBL_RECTIFICATION_ISSUES")
public class TblRectificationIssues implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String FORMTYPE = "TBL_RECTIFICATION_ISSUES";//组织用户关系表中对应的表单类型
    
    public static final String FORMCOL = "协办部门"; //组织用户关系表中对应的 列类型
    

      @Schema(name = "主键")
        @TableId("RELAID")
      @Id
      @Column(name="RELAID")
      private BigDecimal relaId;

      @Schema(name = "整改清单主键")
      @TableField("ISSUESID")
      @Column(name="ISSUESID")
    private String issuesId;

      @Schema(name = "整改方案主键")
      @TableField("PLANID")
      @Column(name="PLANID")
    private String planId;

      @Schema(name = "经办人")
      @TableField("HANDLERID")
      @Column(name="HANDLERID")
    private BigDecimal handlerId;

      @Schema(name = "整改方案")
      @TableField("RECTIFICATIONPLAN")
      @Column(name="RECTIFICATIONPLAN")
    private String rectificationPlan;

      @Schema(name = "整改措施")
      @TableField("RECTIFICATIONMEASURES")
      @Column(name="RECTIFICATIONMEASURES")
    private String rectificationMeasures;

      @Schema(name = "成果体现")
      @TableField("RESULTMEMO")
      @Column(name="RESULTMEMO")
    private String resultMemo;

      @Schema(name = "预计完成时间")
      @TableField("DEADLINE")
      @DateTimeFormat(pattern="yyyy-MM-dd")
      @JSONField(format = "yyyy-MM-dd")
      @Column(name="DEADLINE")
    private Date deadline;

      @Schema(name = "整改落实人")
      @TableField("IMPLEMENTER")
      @Column(name="IMPLEMENTER")
    private BigDecimal implementer;

      @Schema(name = "版本信息")
      @TableField("VERSION")
      @Column(name="VERSION")
    private Integer version;

      @Schema(name = "责任人")
      @TableField("RESPONSIBLEPERSON")
      @Column(name="RESPONSIBLEPERSON")
    private BigDecimal responsiblePerson;

      @Schema(name = "责任部门")
      @TableField("RESPONSIBLEDEPT")
      @Column(name="RESPONSIBLEDEPT")
    private BigDecimal responsibleDept;

      @Schema(name = "数据状态")
      @TableField("STATUS")
      @Column(name="STATUS")
    private Integer status;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @Column(name="CREATETIME")
    private Date createTime;
      
      
      @Schema(name = "附件信息")
      @TableField(exist = false)
      @Transient
      private String[] attIds;
}
