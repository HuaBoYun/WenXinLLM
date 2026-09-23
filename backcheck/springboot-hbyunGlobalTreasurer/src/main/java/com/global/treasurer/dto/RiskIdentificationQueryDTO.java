package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.util.Date;

/**
 * 风险识别查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
public class RiskIdentificationQueryDTO {
    /** 当前页 */
    private Integer current = 1;

    /** 页大小 */
    private Integer size = 10;

    /** 风险编号 */
    private String riskCode;

    /** 风险名称 */
    private String riskName;

    /** 风险类型 */
    private String riskType;

    /** 风险等级 */
    private String riskLevel;

    /** 状态 */
    private String status;

    /** 识别开始日期 */
    private Date identificationDateStart;

    /** 识别结束日期 */
    private Date identificationDateEnd;

    /** 组织ID */
    private Long orgId;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getRiskCode() { return riskCode; }
    public void setRiskCode(String riskCode) { this.riskCode = riskCode; }
    public String getRiskName() { return riskName; }
    public void setRiskName(String riskName) { this.riskName = riskName; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getIdentificationDateStart() { return identificationDateStart; }
    public void setIdentificationDateStart(Date identificationDateStart) { this.identificationDateStart = identificationDateStart; }
    public Date getIdentificationDateEnd() { return identificationDateEnd; }
    public void setIdentificationDateEnd(Date identificationDateEnd) { this.identificationDateEnd = identificationDateEnd; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }


    public int getCurrent() { return current != null ? current : 1; }
    public void setCurrent(int current) { this.current = current; }
    public int getSize() { return size != null ? size : 10; }
    public void setSize(int size) { this.size = size; }
}
