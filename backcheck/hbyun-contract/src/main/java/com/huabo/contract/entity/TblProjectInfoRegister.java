package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目信息登记表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_PROJECT_INFO_REGISTER")
public class TblProjectInfoRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID（主键）
     */
    @TableId(value = "PROJECT_ID", type = IdType.ASSIGN_ID)
    private String projectId;

    /**
     * 登记编号（唯一）
     */
    @TableField("REGISTER_NO")
    private String registerNo;

    /**
     * 项目名称
     */
    @TableField("PROJECT_NAME")
    private String projectName;

    /**
     * 发包方全称
     */
    @TableField("CONTRACTOR_FULL_NAME")
    private String contractorFullName;

    /**
     * 统一社会信用代码
     */
    @TableField("CONTRACTOR_CREDIT_CODE")
    private String contractorCreditCode;

    /**
     * 资金来源
     */
    @TableField("FUNDING_SOURCE")
    private String fundingSource;

    /**
     * 项目金额
     */
    @TableField("PROJECT_AMOUNT")
    private BigDecimal projectAmount;

    /**
     * 预算收入
     */
    @TableField("BUDGET_INCOME")
    private BigDecimal budgetIncome;

    /**
     * 预算支出
     */
    @TableField("BUDGET_EXPENSE")
    private BigDecimal budgetExpense;

    /**
     * 服务内容
     */
    @TableField("SERVICE_CONTENT")
    private String serviceContent;

    /**
     * 是否需要首谈报备（0-否，1-是）
     */
    @TableField("IS_FIRST_TALK_REPORT")
    private Integer isFirstTalkReport;

    /**
     * 报备状态（0-未报备，1-已报备，2-已审核）
     */
    @TableField("REPORT_STATUS")
    private Integer reportStatus;

    /**
     * 项目状态（1-登记，2-承接，3-执行，4-完成）
     */
    @TableField("PROJECT_STATUS")
    private Integer projectStatus;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 登记人ID
     */
    @TableField("REGISTER_USER_ID")
    private String registerUserId;

    /**
     * 登记人姓名
     */
    @TableField("REGISTER_USER_NAME")
    private String registerUserName;

    /**
     * 登记时间
     */
    @TableField("REGISTER_TIME")
    private Date registerTime;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 获取项目状态名称
     */
    public String getProjectStatusName() {
        if (projectStatus == null) {
            return "";
        }
        switch (projectStatus) {
            case 1:
                return "登记";
            case 2:
                return "承接";
            case 3:
                return "执行";
            case 4:
                return "完成";
            default:
                return "未知";
        }
    }

    /**
     * 获取报备状态名称
     */
    public String getReportStatusName() {
        if (reportStatus == null) {
            return "";
        }
        switch (reportStatus) {
            case 0:
                return "未报备";
            case 1:
                return "已报备";
            case 2:
                return "已审核";
            default:
                return "未知";
        }
    }

    /**
     * 判断是否需要首谈报备
     * 项目金额大于等于500万元需要报备
     */
    public boolean needFirstTalkReport() {
        return projectAmount != null && projectAmount.compareTo(new BigDecimal("5000000")) >= 0;
    }
}
