package com.financial.sharing.vo.param;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目台账查询参数
 */
@Data
public class ProjectLedgerQueryParam {
    
    /**
     * 项目编号
     */
    private String projectNo;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 项目状态
     */
    private String projectStatus;
    
    /**
     * 项目类型
     */
    private String projectType;
    
    /**
     * 项目经理
     */
    private String projectManager;
    
    /**
     * 开始日期开始
     */
    private Date startDateStart;
    
    /**
     * 开始日期结束
     */
    private Date startDateEnd;
    
    /**
     * 预算金额最小值
     */
    private BigDecimal budgetAmountMin;
    
    /**
     * 预算金额最大值
     */
    private BigDecimal budgetAmountMax;
    
    /**
     * 所属部门
     */
    private String department;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 页大小
     */
    private Integer pageSize = 10;
}
