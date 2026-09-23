package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 凭证生成进度
 *
 * @author system
 * @since 2024-12-08
 */
@Data
@ApiModel("凭证生成进度")
public class VoucherGenerationProgress {

    @ApiModelProperty(value = "批次ID")
    private String batchId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "生成模式")
    private String generationMode;

    @ApiModelProperty(value = "总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "已完成数量")
    private Integer completedCount;

    @ApiModelProperty(value = "失败数量")
    private Integer failedCount;

    @ApiModelProperty(value = "处理中数量")
    private Integer processingCount;

    @ApiModelProperty(value = "进度百分比")
    private Double progress;

    @ApiModelProperty(value = "任务状态")
    private String status; // PENDING, RUNNING, COMPLETED, FAILED, STOPPED

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "预计结束时间")
    private Date estimatedEndTime;

    @ApiModelProperty(value = "执行时长（秒）")
    private Long duration;

    @ApiModelProperty(value = "任务参数")
    private Map<String, Object> parameters;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "详情列表")
    private List<GenerationDetailVO> details;

    @Data
    @ApiModel("生成详情")
    public static class GenerationDetailVO {
        @ApiModelProperty(value = "事项ID")
        private Long transactionId;

        @ApiModelProperty(value = "事项编号")
        private String transactionNo;

        @ApiModelProperty(value = "凭证ID")
        private Long voucherId;

        @ApiModelProperty(value = "凭证号")
        private String voucherNo;

        @ApiModelProperty(value = "生成状态")
        private String generationStatus; // COMPLETED, FAILED, PROCESSING

        @ApiModelProperty(value = "错误信息")
        private String errorMessage;

        @ApiModelProperty(value = "生成时间")
        private Date generationTime;

        @ApiModelProperty(value = "重试次数")
        private Integer retryCount;
    }
}