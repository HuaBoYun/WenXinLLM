package com.financial.sharing.vo.result;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目台账VO
 */
@Data
public class ProjectLedgerVO {
    
    /**
     * 项目ID
     */
    private String projectId;
    
    /**
     * 项目编号
     */
    private String projectNo;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 项目类型
     */
    private String projectType;
    
    /**
     * 项目状态
     */
    private String projectStatus;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 项目描述
     */
    private String projectDescription;
    
    /**
     * 项目经理
     */
    private String projectManager;
    
    /**
     * 所属部门
     */
    private String department;
    
    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;
    
    /**
     * 已使用金额
     */
    private BigDecimal usedAmount;
    
    /**
     * 剩余金额
     */
    private BigDecimal remainingAmount;
    
    /**
     * 币种
     */
    private String currency;
    
    /**
     * 计划开始日期
     */
    private Date plannedStartDate;
    
    /**
     * 计划结束日期
     */
    private Date plannedEndDate;
    
    /**
     * 实际开始日期
     */
    private Date actualStartDate;
    
    /**
     * 实际结束日期
     */
    private Date actualEndDate;
    
    /**
     * 项目进度
     */
    private BigDecimal projectProgress;
    
    /**
     * 预算执行率
     */
    private BigDecimal budgetExecutionRate;
    
    /**
     * 项目成员
     */
    private List<String> projectMembers;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 备注
     */
    private String remark;
}
