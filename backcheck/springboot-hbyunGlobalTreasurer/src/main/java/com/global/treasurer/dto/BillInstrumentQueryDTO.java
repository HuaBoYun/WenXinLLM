package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 票据查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillInstrumentQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 票据编号 */
    private String instrumentNumber;

    /** 票据类型 */
    private String instrumentType;

    /** 票据状态 */
    private String instrumentStatus;

    /** 出票人 */
    private String drawer;

    /** 收款人 */
    private String payee;

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


    // 以下方法由Lombok生成,手动添加以解决编译问题
    public String getInstrumentNumber() { return instrumentNumber; }
    public void setInstrumentNumber(String instrumentNumber) { this.instrumentNumber = instrumentNumber; }
    public String getInstrumentType() { return instrumentType; }
    public void setInstrumentType(String instrumentType) { this.instrumentType = instrumentType; }
    public String getInstrumentStatus() { return instrumentStatus; }
    public void setInstrumentStatus(String instrumentStatus) { this.instrumentStatus = instrumentStatus; }
    public String getDrawer() { return drawer; }
    public void setDrawer(String drawer) { this.drawer = drawer; }
    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
