package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.sql.Timestamp;

/**
 * 市场数据实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GT_MARKET_DATA")
public class TblGtMarketData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @TableId(value = "DATA_ID", type = IdType.AUTO)
    private Long dataId;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据代码
     */
    private String symbol;

    /**
     * 数据名称
     */
    private String symbolName;

    /**
     * 数据值
     */
    private BigDecimal dataValue;

    /**
     * 数据日期
     */
    private LocalDate dataDate;

    /**
     * 数据时间
     */
    private Timestamp dataTime;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 是否有效
     */
    private Integer isActive;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getDataId() { return dataId; }
    public void setDataId(Long dataId) { this.dataId = dataId; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getSymbolName() { return symbolName; }
    public void setSymbolName(String symbolName) { this.symbolName = symbolName; }
    public BigDecimal getDataValue() { return dataValue; }
    public void setDataValue(BigDecimal dataValue) { this.dataValue = dataValue; }
    public LocalDate getDataDate() { return dataDate; }
    public void setDataDate(LocalDate dataDate) { this.dataDate = dataDate; }
    public Timestamp getDataTime() { return dataTime; }
    public void setDataTime(Timestamp dataTime) { this.dataTime = dataTime; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
