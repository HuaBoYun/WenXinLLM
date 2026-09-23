package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

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
 * 整改清单表
 * </p>
 *
 * @author LHP
 * @since 2023-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ZGZZ_ISSUESILIST")
@Schema(name="TblZgzzIssuesilist对象", description="整改清单表")
@Table(name="TBL_ZGZZ_ISSUESILIST")
public class TblZgzzIssuesilist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "整改清单主键")
    @TableId("ISSUESID")
    @Column(name = "ISSUESID")
    @Id
    private String issuesId;

      @Schema(name = "业务编号")
      @TableField("ISSUESCODE")
      @Column(name = "ISSUESCODE")
    private String issuesCode;
      
      @Schema(name = "问题编号")
      @TableField("PROCODE")
      @Column(name = "PROCODE")
    private String proCode;

      @Schema(name = "审计内控关联表单外键")
      @TableField("QUESITIONID")
      @Column(name = "QUESITIONID")
    private BigDecimal quesitionId;

      @Schema(name = "业务名称")
      @TableField("ISSUESNAME")
      @Column(name = "ISSUESNAME")
    private String issuesName;

      @Schema(name = "创建人")
      @TableField("CREATESTAFF")
      @Column(name = "CREATESTAFF")
    private BigDecimal createStaff;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
      @Column(name = "CREATETIME")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;

      @Schema(name = "关联项目主键")
      @TableField("PROJECTID")
      @Column(name = "PROJECTID")
    private BigDecimal projectId;

      @Schema(name = "修改时间")
      @TableField("UPDATETIME")
      @Column(name = "UPDATETIME")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;

      @Schema(name = "事项")
      @TableField("ISSUESITEM")
      @Column(name = "ISSUESITEM")
    private String issuesItem;

      @Schema(name = "问题标题")
      @TableField("ISSUESTITLE")
      @Column(name = "ISSUESTITLE")
    private String issuesTitle;

      @Schema(name = "审计执行过程")
      @TableField("PROGRAMPROCESS")
      @Column(name = "PROGRAMPROCESS")
    private String programProcess;

    @TableField("QUESTIONMEMO")
    @Column(name = "QUESTIONMEMO")
    @Schema(name = "问题详情")
    private String questionMemo;

      @Schema(name = "审计意见及建议")
      @TableField("OPINIONS")
      @Column(name = "OPINIONS")
    private String opinions;

      @Schema(name = "业务类别 1-审计 2-风控 3-外部")
      @TableField("ISSUESTYPE")
      @Column(name = "ISSUESTYPE")
    private Integer issuesType;

    @TableField("STATUS")
    @Column(name = "STATUS")
    @Schema(name = "审批状态 0-未整改 1-审批中 ，2-已退回 3-已撤销 6-已完成 ，7-整改中 ，8-整改完成，9-未销号问题 、 10-再次整改 , 11-关闭")
    private Integer status;

      @Schema(name = "历史版本")
      @TableField("ISSUESVERSION")
      @Column(name = "ISSUESVERSION")
    private Integer issuesVersion;

      @Schema(name = "变更前的主键")
      @TableField("ISSUESPARENT")
      @Column(name = "ISSUESPARENT")
    private String issuesParent;
      
      @Schema(name = "所属公司")
      @TableField("LINKORGID")
      @Column(name = "LINKORGID")
    private BigDecimal linkOrgId;
      
      @Schema(name = "所属部门")
      @TableField("LINKDEPTID")
      @Column(name = "LINKDEPTID")
    private BigDecimal linkDeptId;
      
      @Schema(name = "被审计对象主键")
      @TableField("AUDITOBJECTID")
      @Column(name = "AUDITOBJECTID")
    private BigDecimal auditObjectId;
      
      @Schema(name = "被审计对象类型 1公司 2部门 3用户")
      @TableField("AUDITOBJECTTYPE")
      @Column(name = "AUDITOBJECTTYPE")
    private Integer auditObjectType;
      
      @Schema(name = "历史状态用于还原")
      @TableField("HISTORYSTATUS")
      @Column(name = "HISTORYSTATUS")
    private Integer historyStatus;
      
      @Schema(name = "责任人")
      @TableField("RESPONSIBLEPERSON")
      @Column(name = "RESPONSIBLEPERSON")
    private BigDecimal responsiblePerson;
      
      @Schema(name = "责任部门")
      @TableField("RESPONSIBLEDEPT")
      @Column(name = "RESPONSIBLEDEPT")
    private BigDecimal responsibleDept;
      
      
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

}
