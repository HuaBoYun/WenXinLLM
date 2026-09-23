package com.global.treasurer.vo.param;

// import lombok.Data; // 已移除,使用手动编写的getter/setter

/**
 * Ukey厂商查询参数
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
// @Data // 已移除,使用手动编写的getter/setter
public class TblUkeyVendorQueryParam {
    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 厂商编码
     */
    private String vendorCode;

    /**
     * 厂商名称
     */
    private String vendorName;

    /**
     * 厂商类型
     */
    private String vendorType;

    /**
     * 合作状态
     */
    private String cooperationStatus;

    // 完整的getter和setter方法

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public String getCooperationStatus() {
        return cooperationStatus;
    }

    public void setCooperationStatus(String cooperationStatus) {
        this.cooperationStatus = cooperationStatus;
    }
}