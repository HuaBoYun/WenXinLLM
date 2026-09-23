package com.global.treasurer.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 债券发行查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Data
public class BondIssuanceQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 发行编号 */
    private String issuanceNo;

    /** 债券名称 */
    private String bondName;

    /** 债券类型 */
    private String bondType;

    /** 发行状态 */
    private String issuanceStatus;

    /** 公司ID */
    private Long companyId;

    /** 币种 */
    private String currencyCode;

    /** 发行日期开始 */
    private String startDate;

    /** 发行日期结束 */
    private String endDate;

    // 显式添加getter方法以确保编译通过
    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getIssuanceNo() {
        return issuanceNo;
    }

    public String getBondName() {
        return bondName;
    }

    public String getBondType() {
        return bondType;
    }

    public String getIssuanceStatus() {
        return issuanceStatus;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}

