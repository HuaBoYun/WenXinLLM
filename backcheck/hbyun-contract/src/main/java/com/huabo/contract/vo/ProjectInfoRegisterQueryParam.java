package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目信息登记查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectInfoRegisterQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登记编号
     */
    private String registerNo;

    /**
     * 项目名称（支持模糊查询）
     */
    private String projectName;

    /**
     * 发包方全称（支持模糊查询）
     */
    private String contractorFullName;

    /**
     * 统一社会信用代码
     */
    private String contractorCreditCode;

    /**
     * 资金来源
     */
    private String fundingSource;

    /**
     * 项目金额最小值
     */
    private BigDecimal projectAmountMin;

    /**
     * 项目金额最大值
     */
    private BigDecimal projectAmountMax;

    /**
     * 项目状态
     */
    private Integer projectStatus;

    /**
     * 报备状态
     */
    private Integer reportStatus;

    /**
     * 是否需要首谈报备
     */
    private Integer isFirstTalkReport;

    /**
     * 登记开始时间
     */
    private Date registerTimeStart;

    /**
     * 登记结束时间
     */
    private Date registerTimeEnd;

    /**
     * 创建开始时间
     */
    private Date createTimeStart;

    /**
     * 创建结束时间
     */
    private Date createTimeEnd;

    /**
     * 项目开始时间（查询起始）
     */
    private Date projectStartTimeBegin;

    /**
     * 项目开始时间（查询结束）
     */
    private Date projectStartTimeEnd;

    /**
     * 项目结束时间（查询起始）
     */
    private Date projectEndTimeBegin;

    /**
     * 项目结束时间（查询结束）
     */
    private Date projectEndTimeEnd;

    /**
     * 登记人ID
     */
    private String registerUserId;

    /**
     * 登记人姓名
     */
    private String registerUserName;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 页码（从1开始）
     */
    private Integer pageNumber = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 20;

    /**
     * 获取页码（兼容方法）
     */
    public Integer getPageNum() {
        return pageNumber;
    }

    /**
     * 设置页码（兼容方法）
     */
    public void setPageNum(Integer pageNum) {
        this.pageNumber = pageNum;
    }

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection = "DESC";

    /**
     * 关键字搜索（项目名称、发包方名称）
     */
    private String keyword;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 项目规模
     */
    private String projectScale;

    /**
     * 是否删除（0-否，1-是）
     */
    private Integer isDeleted = 0;
}
