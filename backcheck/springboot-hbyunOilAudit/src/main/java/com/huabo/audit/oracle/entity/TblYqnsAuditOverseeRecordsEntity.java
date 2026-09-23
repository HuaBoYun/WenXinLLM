package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditOverseeRecordsEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/10 11:03.
 * @version: V1.0
 * @description: 央企内审-审计实施-审计督导记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value="AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_AUDIT_OVERSEE_RECORDS")
@Schema(name="TblYqnsAuditOverseeRecords对象", description="央企内审-审计实施-审计督导记录")
public class TblYqnsAuditOverseeRecordsEntity extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审计督导记录主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "审计督导记录主键ID")
    private Long id;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField("PROJECTNAME")
    private String projectName ;


    /**
     * 主审
     */
    @Schema(name = "主审")
    @TableField("CHIEFAUDITOR")
    private String chiefAuditor ;

    /**
     * 助审
     */
    @Schema(name = "助审")
    @TableField("ASSESSORS")
    private String assessors ;

    /**
     * 经责科负责人
     */
    @Schema(name = "经责科负责人")
    @TableField("ECONOMICNAME")
    private String economicName ;
    
    @Schema(name = "经责科负责人主键")
    @TableField("ECONOMICID")
    private String  economicId;

    /**
     * 督导方式
     */
    @Schema(name = "督导方式")
    @TableField("SUPERVISION")
    private String supervision ;



    /**
     * 参与督导人员
     */
    @Schema(name = "参与督导人员")
    @TableField("SUPERVISIONPARTICIPANTS")
    private String supervisionParticipants ;


    /**
     * 参与督导人员ID
     */
    @Schema(name = "参与督导人员ID")
    @TableField("SUPERVISIONPARTICIPANTSID")
    private String supervisionParticipantsId ;




    /**
     * 督导日期
     */
    @Schema(name = "督导日期")
    @TableField("SUPERVISIONDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date supervisionDate;

    /**
     * 审计实施方案中审计内容及重点
     */
    @Schema(name = "审计实施方案中审计内容及重点")
    @TableField("AUDITCONTENTANDFOCUS")
    private String auditContentAndFocus ;

    /**
     * 组织机构
     */ 
    @Schema(name = "组织机构")
    @TableField(exist = false)
    private TblOrganization tblOrganization; 


    /**
     * 单位
     */
    @Schema(name = "单位")
    @TableField("ORGID")
    private BigDecimal orgId ;


    /**
     * 计划时间安排
     */
    @Schema(name = "计划时间安排")
    @TableField("PLANNEDTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date plannedTime;

    /**
     * 人员组织
     */
    @Schema(name = "人员组织")
    @TableField(exist = false)
    private TblStaff tblStaff;


    /**
     * 审计人员
     */
    @Schema(name = "审计人员")
    @TableField("AUDITSTAFFID")
    private BigDecimal auditStaffId;

    /**
     * 审计任务执行情况
     */
    @Schema(name = "审计任务执行情况")
    @TableField("EXECUTIONSITUATION")
    private String executionSituation;


    /**
     * 现场情况
     */
    @Schema(name = "现场情况")
    @TableField("ONSITECONDITION")
    private String onsiteCondition ;

    /**
     * 审计组发现问题
     */
    @Schema(name = "审计组发现问题")
    @TableField("DISCOVERPROBLEMS")
    private String discoverProblems;

    /**
     * 存在问题及需协调解决的问题
     */
    @Schema(name = "存在问题及需协调解决的问题")
    @TableField("ISSUESANDCOORDINATION")
    private String issuesAndCoordination ;


    /**
     * 督导意见
     */
    @Schema(name = "督导意见")
    @TableField("SUPERVISIONOPINIONS")
    private String supervisionOpinions ;


    /**
     * 审计组是否采纳
     */
    @Schema(name = "审计组是否采纳")
    @TableField("ISADOPT")
    private String isAdopt;

    /**
     * 未采纳原因或采纳结果
     */
    @Schema(name = "未采纳原因或采纳结果")
    @TableField("ISADOPTRESULT")
    private String isAdoptResult;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks ;

    /**
     * 备注
     */
    @Schema(name = "审批状态")
    @TableField("STATUS")
    private Integer status ;




    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField("CREATEUSER")
    private String createUser;


    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @Schema(name = "修改人")
    @TableField("UPDATEUSER")
    private String updateUser;

    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 删除标记 默认：0 删除：1
     */
    @Schema(name = "删除标记 默认：0 删除：1")
    @TableField("DELFLAG")
    private Integer delFlag;


    /**
     * 项目id
     */
    @Schema(name = "项目id")
    @TableField("PROJECTID")
    private BigDecimal projectId;

    @Schema(name = "序号")
    @TableField("SXNUMBER")
    private String sxnumber ;
    
    @Schema(name = "创建人主键")
    @TableField("CREATESTAFFID")
    private BigDecimal createStaffId;
    
    
    @TableField(exist = false)
    private TblYqnsXmdq xmqd;
    
}
