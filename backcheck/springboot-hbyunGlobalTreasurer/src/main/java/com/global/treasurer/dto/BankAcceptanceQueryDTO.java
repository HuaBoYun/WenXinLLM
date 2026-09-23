package com.global.treasurer.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行承兑汇票查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Data
public class BankAcceptanceQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 承兑汇票编号 */
    private String acceptanceNumber;

    /** 承兑状态 */
    private String acceptanceStatus;

    /** 出票人 */
    private String drawer;

    /** 收款人 */
    private String payee;

    /** 承兑银行 */
    private String acceptingBank;

    /** 公司ID */
    private String companyId;

    /** 开始日期 */
    private Date startDate;

    /** 结束日期 */
    private Date endDate;

    /** 页码 */
    private Integer pageNum;

    /** 每页数量 */
    private Integer pageSize;

    // 显式添加getter方法以确保编译通过
    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}

