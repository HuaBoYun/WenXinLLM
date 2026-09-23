package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Transient;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NBSJ_STAFFSCORE")
@Schema(name="TBL_NBSJ_STAFFSCORE对象", description="审计-人员评分表")
public class TblNbsjStaffscore implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public final static Integer STATE_SP = 1;//审批中
    public final static Integer STATE_TZ = 2;//调整
    public final static Integer STATE_TG = 3;//已通过
    public final static Integer STATE_ZZ = 4;//已终止
    public final static Integer STATE_GZ = 5;//已跟踪
    public final static Integer STATE_WC = 6;//已完成
    
    public final static String settingId = "PJSH";//已完成
    public final static String settingUrl = "/audit/nbsjapproval/getStaffScoreApprovalInfo?staffScoreid=";//已完成

    @TableId(value = "staffScore_id", type= IdType.AUTO)
    @Schema(name = "人员评分表主键")
    private BigDecimal staffScoreid;

    @TableField(value = "unit")
    @Schema(name = "单位id")
    private BigDecimal unit;

    @TableField(value = "auditors")
    @Schema(name = "审计人员id")
    private BigDecimal auditors;
    
    @Schema(name = "审计人员实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblStaff auditor;

    @TableField(value = "auditProjectName")
    @Schema(name = "参与审计项目名称")
    private String auditProjectName;

    @TableField(value = "totalScore")
    @Schema(name = "总分")
    private String totalScore;

    @TableField(value = "assessmentResults")
    @Schema(name="考核结果",hidden=true)
    private String assessmentResults;
    
    
    @TableField("PROJECTID")
    @Schema(name = "审计项目ID")
    private BigDecimal projectid;
    
    @TableField("STATUS")
    @Schema(name = "审核状态  1.未审核 2.审核中 3.需调整 4.已完成")
    private Integer status;

//    @TableField(value = "ZbAuditManagerVerifyScore")
//    @Schema(name = "总部审计管理人员核实评分")
//    private String ZbAuditManagerVerifyScore;
//
//    @TableField(value = "ZbAuditManagerVerifyResults")
//    @Schema(name = "总部审计管理人员考核结果")
//    private String ZbAuditManagerVerifyResults;
//
//    @TableField(value = "ZbAuditManagerVerifyOpinion")
//    @Schema(name = "总部审计管理人员考核意见")
//    private String ZbAuditManagerVerifyOpinion;
//
//    @TableField(value = "ZbAuditDepartmentScoring")
//    @Schema(name = "总部审计部门负责人意见评分")
//    private String ZbAuditDepartmentScoring;
//
//    @TableField(value = "ZbAuditDepartmentResults")
//    @Schema(name = "总部审计部门负责人考核结果")
//    private String ZbAuditDepartmentResults;
//
//    @TableField(value = "ZbAuditDepartmentOpinion")
//    @Schema(name = "总部审计部门负责人意见")
//    private String ZbAuditDepartmentOpinion;
//
//    @TableField(value = "tblNbsjStaffscoreDetails对象")
//    @Schema(name = "审计-人员评分明细表")
//    private List<TblNbsjStaffscoreDetails> tblNbsjStaffscoreDetails;
    
    @Schema(name = "人员评分明细表")
    @Transient
    @IgnoreSwaggerParameter
    private List<TblNbsjStaffscoreDetails> tblNbsjStaffscoreDetails;
    

}
