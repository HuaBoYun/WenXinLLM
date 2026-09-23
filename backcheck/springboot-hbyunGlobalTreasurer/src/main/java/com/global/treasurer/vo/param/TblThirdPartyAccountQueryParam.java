package com.global.treasurer.vo.param;

// import lombok.Data; // 已移除

/**
 * 第三方账户查询参数
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
// @Data // 已移除,使用手动编写的getter/setter
public class TblThirdPartyAccountQueryParam {
    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 账户编码
     */
    private String accountCode;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 第三方系统
     */
    private String thirdPartySystem;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * 连接状态
     */
    private String connectionStatus;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getAccountCode() { return accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getThirdPartySystem() { return thirdPartySystem; }
    public void setThirdPartySystem(String thirdPartySystem) { this.thirdPartySystem = thirdPartySystem; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getConnectionStatus() { return connectionStatus; }
    public void setConnectionStatus(String connectionStatus) { this.connectionStatus = connectionStatus; }


    public int getPageNo() { return pageNo != null ? pageNo : 1; }
    public void setPageNo(int pageNo) { this.pageNo = pageNo; }
    public int getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
