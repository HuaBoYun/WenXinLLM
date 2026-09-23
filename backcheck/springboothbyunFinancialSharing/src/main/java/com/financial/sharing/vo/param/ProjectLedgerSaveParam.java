package com.financial.sharing.vo.param;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目台账保存参数
 */
@Data
public class ProjectLedgerSaveParam {
    
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
     * 项目成员
     */
    private List<String> projectMembers;
    
    /**
     * 备注
     */
    private String remark;
}
