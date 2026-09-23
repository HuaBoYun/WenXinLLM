package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 期末处理检查结果
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Data
@ApiModel("期末处理检查结果")
public class PeriodEndCheckResult {

    @ApiModelProperty(value = "会计期间")
    private String accountingPeriod;

    @ApiModelProperty(value = "检查任务ID")
    private String checkTaskId;

    @ApiModelProperty(value = "检查状态", example = "COMPLETED")
    private String checkStatus; // PENDING, PROCESSING, COMPLETED, FAILED

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "总检查项数")
    private Integer totalItems;

    @ApiModelProperty(value = "通过检查项数")
    private Integer passedCount;

    @ApiModelProperty(value = "失败检查项数")
    private Integer failedCount;

    @ApiModelProperty(value = "警告检查项数")
    private Integer warningCount;

    @ApiModelProperty(value = "是否可以继续下一步操作")
    private Boolean canProceed;

    @ApiModelProperty(value = "检查项详情")
    private List<CheckItem> checkItems;

    // 单个检查项的字段（用于executePeriodEndCheck方法）
    @ApiModelProperty(value = "检查项ID")
    private String checkId;

    @ApiModelProperty(value = "检查项名称")
    private String checkItemName;

    @ApiModelProperty(value = "检查项类型")
    private String checkType;

    @ApiModelProperty(value = "检查描述")
    private String checkDescription;

    @ApiModelProperty(value = "是否必需检查")
    private String isRequired;

    @ApiModelProperty(value = "检查顺序")
    private Integer checkOrder;

    @ApiModelProperty(value = "检查结果")
    private String checkResult;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @Data
    @ApiModel("检查项")
    public static class CheckItem {
        @ApiModelProperty(value = "检查项ID")
        private String checkId;

        @ApiModelProperty(value = "检查项名称")
        private String checkName;

        @ApiModelProperty(value = "检查项类型")
        private String checkType;

        @ApiModelProperty(value = "检查描述")
        private String description;

        @ApiModelProperty(value = "是否必需检查")
        private Boolean isRequired;

        @ApiModelProperty(value = "检查状态")
        private String status; // PASSED, FAILED, WARNING, SKIPPED

        @ApiModelProperty(value = "状态名称")
        private String statusName;

        @ApiModelProperty(value = "检查结果描述")
        private String checkResult;

        @ApiModelProperty(value = "错误信息")
        private String errorMessage;

        @ApiModelProperty(value = "检查时间")
        private Date checkTime;
    }
}