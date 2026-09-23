package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;

/**
 * 票据池查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel("票据池查询DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPoolQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("票据池ID")
    private Long poolId;

    @ApiModelProperty("票据池名称")
    private String poolName;

    @ApiModelProperty("票据池编码")
    private String poolCode;

    @ApiModelProperty("票据池类型")
    private String poolType;

    @ApiModelProperty("所有者ID")
    private Long ownerId;

    @ApiModelProperty("池状态")
    private String poolStatus;

    @ApiModelProperty("页码")
    private Integer pageNum = 1;

    @ApiModelProperty("每页数量")
    private Integer pageSize = 10;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getPoolId() { return poolId; }
    public void setPoolId(Long poolId) { this.poolId = poolId; }
    public String getPoolName() { return poolName; }
    public void setPoolName(String poolName) { this.poolName = poolName; }
    public String getPoolCode() { return poolCode; }
    public void setPoolCode(String poolCode) { this.poolCode = poolCode; }
    public String getPoolType() { return poolType; }
    public void setPoolType(String poolType) { this.poolType = poolType; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getPoolStatus() { return poolStatus; }
    public void setPoolStatus(String poolStatus) { this.poolStatus = poolStatus; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
