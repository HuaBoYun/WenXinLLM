package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 凭证生成结果
 *
 * @author system
 * @since 2024-12-08
 */
@Data
@ApiModel("凭证生成结果")
public class VoucherGenerationVO {

    @ApiModelProperty(value = "批次ID")
    private String batchId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "生成模式")
    private String generationMode;

    @ApiModelProperty(value = "生成策略")
    private String generationStrategy;

    @ApiModelProperty(value = "总生成数量")
    private Integer totalCount;

    @ApiModelProperty(value = "预计耗时（秒）")
    private Long estimatedTime;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "消息")
    private String message;
}