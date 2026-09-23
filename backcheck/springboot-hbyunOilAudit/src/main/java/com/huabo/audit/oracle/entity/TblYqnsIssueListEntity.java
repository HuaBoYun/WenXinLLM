package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_ISSUE_LIST")
@Schema(name="TblYqnsIssueList对象")
public class TblYqnsIssueListEntity extends BaseReservedProperty implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID")
    @Schema(name = "问题清单表主键ID")
    private BigDecimal id;


    @Schema(name = "在报告中对应的编号 ")
    @TableField("ISSUENUMBER")
    private String issueNumber;
    
    
    @Schema(name = "问题涉及企业的管理层级")
    @TableField("WTSJGLCJ")
    private String wtsjglcj;
    
    @Schema(name = "资产损失（万元）")
    @TableField("ASSETLOSS")
    private BigDecimal assetLoss;
    

    @Schema(name = "风险程度")
    @TableField("RISKLEVEL")
    private String riskLevel;
    
    
    /**
     * 审计报告定稿主键
     */
    @Schema(name = "审计报告定稿主键")
    @TableField(value = "SJBGDGID")
    private Long sjbgdgid;
    
    /**
     * 标题
     */
    @Schema(name = "审计报告定稿标题")
    @TableField(value = "SJBGDGTITLE")
    private String sjbgdgTitle;
    


    @Schema(name = "问题所属单位名称")
    @TableField("UNITNAME")
    private String unitName;
    
    @Schema(name = "问题所属单位主键")
    @TableField("UNITORGID")
    private BigDecimal unitOrgId;
    

    @Schema(name = "事实表述")
    @TableField("ISSUEDETAIL")
    private String issueDetail;


    @Schema(name = "问题金额")
    @TableField("MONEY")
    private BigDecimal money;
    
    @Schema(name = "审减金额")
    @TableField("REVIEWMONEY")
    private BigDecimal reviewMoney;
    
    @Schema(name = "问题定性(审计报告定稿中的问题定性)")
    @TableField("PROBLEMQUALITATIVE")
    private String problemQualitative;


    @Schema(name = "定性（定性词典）")
    @TableField("QUALITATIVE")
    private String qualitative;


    @Schema(name = "定性法规依据")
    @TableField("QUALITATIVERULE")
    private String qualitativeRule;


    @Schema(name = "处理意见或整改建议")
    @TableField("CORRECTPROPOSE")
    private String correctPropose;


    @Schema(name = "整改时限")
    @TableField("TIMELIMIT")
    private String timeLimit;
    
    @TableField(value = "PROJECTID")
    @Schema(name = "审计项目ID")
    private BigDecimal projectId;
    
    @TableField(value = "PROJECTNAME")
    @Schema(name = "审计项目名称")
    private String projectName;


    @Schema(name = "整改督促牵头部门或单位")
    @TableField("URGEDEPARTMENT")
    private String urgeDepartment;


    @Schema(name = "整改负责人")
    @TableField("HEAD")
    private String head;
    
    @Schema(name = "整改负责人主键")
    @TableField("HEADERID")
    private BigDecimal headerId;
    
    @Schema(name = "整改人姓名")
    @TableField("RECTPERNAME")
    private String rectPerName;
    
    @Schema(name = "整改人主键")
    @TableField("RECTPERSON")
    private BigDecimal rectPerson;

    @Schema(name = "创建人")
    @TableField("OPERATOR")
    private String operator;


    @Schema(name = "关联底稿名称")
    @TableField("DRAFT")
    private String draft;
    
    @Schema(name = "关联底稿主键（多个用逗号拼接）")
    @TableField("DRAFTIDSTRS")
    private String draftIdStrs;


    @Schema(name = "添加时间")
    @TableField("ADDTIME")
    private Date addTime;

    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    private Date updateTime;


    @Schema(name = "操作员ID")
    @TableField("OPERATORID")
    private BigDecimal operatorId;

    @Schema(name="附件文件ID")
    @TableField(exist = false)
    private String fileIds;

    @Schema(name = "整改分类")
    @TableField("RECTCLASS")
    private String rectClass;
    
    @Schema(name = "问题发生年度")
    @TableField("PROBLEMYEAR")
    private String problemYear;
    
    @Schema(name = "问题创建所属部门")
    @TableField("LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name = "问题创建所属公司")
    @TableField("LINKORGID")
    private BigDecimal linkOrgId;
    
    @Schema(name="审批状态  0-未审批 ，1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、")
    @TableField("STATUS ")
    private Integer status;
    
    @Schema(name = "审计建议列表")
    @TableField(exist = false)
    private List<TblYqnsProposeEntity> proList;
}
