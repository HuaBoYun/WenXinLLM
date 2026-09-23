package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 导出任务查询参数
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@ApiModel("导出任务查询参数")
public class ExportTaskQueryParam {

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "导出类型")
    private String exportType;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String orderBy = "CREATE_TIME";

    @ApiModelProperty(value = "排序方向")
    private String orderDirection = "DESC";

    @ApiModelProperty(value = "导出类型列表")
    private List<String> exportTypes;
}