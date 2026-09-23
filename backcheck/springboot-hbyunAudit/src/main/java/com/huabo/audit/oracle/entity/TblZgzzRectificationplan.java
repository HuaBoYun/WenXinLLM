package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 整改F方案表
 * </p>
 *
 * @author LHP
 * @since 2023-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ZGZZ_RECTIFICATIONPLAN")
@Table(name = "TBL_ZGZZ_RECTIFICATIONPLAN")
@Schema(name="TblZgzzRectificationplan对象", description="整改方案表")
public class TblZgzzRectificationplan implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "整改方案主键")
      @TableId("PLANID")
      @Column(name = "PLANID")
      @Id
      private BigDecimal planId;

      @Schema(name = "整改方案编号")
      @TableField("PLANCODE")
      @Column(name = "PLANCODE")
    private String planCode;

      @Schema(name = "整改方案名称")
      @TableField("PLANNAME")
      @Column(name = "PLANNAME")
    private String planName;

      @Schema(name = "方案类别 1-审计 2-风控 3-外部 ,4-外部审计")
      @TableField("PLANTYPE")
      @Column(name = "PLANTYPE")
    private Integer planType;

      @Schema(name = "关联项目主键")
      @TableField("PROJECTID")
      @Column(name = "PROJECTID")
    private BigDecimal projectId;

      @Schema(name = "截止时间")
      @TableField("DEADLINETIME")
      @Column(name = "DEADLINETIME")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date deadlineTime;

      @Schema(name = "备注")
      @TableField("PLANMEMO")
      @Column(name = "PLANMEMO")
    private String planMemo;

      @Schema(name = "整改责任人主键")
      @TableField("RESPONSE")
      @Column(name = "RESPONSE")
    private BigDecimal response;

      @Schema(name = "方案 状态 0-未审批 1-审批中 ，2-已退回 3-已撤销  6-审批完未启动  7-已启动未下发 8-已下发未分派  9-分派完成 开始整改 、10-已完成、11-关闭, 12-到期未整改 ")
      @TableField("STATUS")
      @Column(name = "STATUS")
    private Integer status;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
      @Column(name = "CREATETIME")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;

      @Schema(name = "修改时间")
      @TableField("UPDATETIME")
      @Column(name = "UPDATETIME")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;

      @Schema(name = "方案所属公司主键")
      @TableField("LINKORGID")
      @Column(name = "LINKORGID")
    private BigDecimal linkOrgId;

      @Schema(name = "数据所属部门主键")
      @TableField("LINKDEPTID")
      @Column(name = "LINKDEPTID")
    private BigDecimal linkDeptId;

      @Schema(name = "创建人主键")
      @TableField("CREATESTAFF")
      @Column(name = "CREATESTAFF")
    private BigDecimal createStaff;

      @Schema(name = "修改人主键")
      @TableField("UPDATESTAFF")
      @Column(name = "UPDATESTAFF")
    private BigDecimal updateStaff;
      
      @Schema(name = "整改经办人主键")
      @TableField("HANDLERID")
      @Column(name = "HANDLERID")
      private BigDecimal handlerId;
      
      @Schema(name = "整改方案百度编辑内用")
      @TableField("ZGCONT")
      @Column(name = "ZGCONT")
      private String zgcont;
      
      @Schema(name = "密级主键")
      @TableField("SECRECTLEVELID")
      @Column(name = "SECRECTLEVELID")
      private BigDecimal secrectLevelId;
      
      @Schema(name = "知悉范围 多个逗号分隔")
      @TableField("STAFFSCOPEIDS")
      @Column(name = "STAFFSCOPEIDS")
      private String staffScopeIds;
      
      @Schema(name = "知悉访问人员姓名 多个逗号分隔")
      @TableField("STAFFSCOPENAMES")
      @Column(name = "STAFFSCOPENAMES")
      private String staffScopeNames;
      
      @TableField(exist = false)
      @Transient
      private String[] attIds;
      
      @TableField(exist = false)
      @Transient
      private List<TblRectificationIssues> relaList;
      
      
      @Schema(name = "整改方式:按问题整改或按方案整改")
      @TableField("ZGFS")
      @Column(name = "ZGFS")
      private String zgfs;
      

}
