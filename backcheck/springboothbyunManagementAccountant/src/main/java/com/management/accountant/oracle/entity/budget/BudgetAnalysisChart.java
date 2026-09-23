package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算分析图表实体
 */
@Data
@TableName("TBL_BUDGET_ANALYSIS_CHART")
public class BudgetAnalysisChart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CHART_ID", type = IdType.ASSIGN_UUID)
    private String chartId;

    @TableField("CHART_NAME")
    private String chartName;

    /** LINE/BAR/PIE/SCATTER/RADAR/HEATMAP/TREEMAP/SANKEY */
    @TableField("CHART_TYPE")
    private String chartType;

    /** BUDGET_DATA/EXECUTION_DATA/ANALYSIS_DATA/FORECAST_DATA */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /** 图表配置JSON */
    @TableField("CHART_CONFIG")
    private String chartConfig;

    /** 缩略图URL */
    @TableField("THUMBNAIL")
    private String thumbnail;

    @TableField("DESCRIPTION")
    private String description;

    /** PUBLISHED/DRAFT/ARCHIVED */
    @TableField("STATUS")
    private String status;

    @TableField("USAGE_COUNT")
    private Integer usageCount;

    @TableField("CREATOR")
    private String creator;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("DEL_FLAG")
    private Integer delFlag;

    // 显式 getter/setter 防止 Lombok 处理失败
    public String getChartId() { return chartId; }
    public void setChartId(String chartId) { this.chartId = chartId; }
    public String getChartName() { return chartName; }
    public void setChartName(String chartName) { this.chartName = chartName; }
    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public String getChartConfig() { return chartConfig; }
    public void setChartConfig(String chartConfig) { this.chartConfig = chartConfig; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }
}
