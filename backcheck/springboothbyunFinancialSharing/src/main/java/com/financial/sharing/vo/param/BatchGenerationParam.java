package com.financial.sharing.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 批量生成凭证参数
 *
 * @author system
 * @since 2024-12-08
 */
@Data
@ApiModel("批量生成凭证参数")
public class BatchGenerationParam {

    @ApiModelProperty(value = "事项ID列表")
    private List<Long> transactionIds;

    @ApiModelProperty(value = "生成模式", example = "BY_DATE")
    @NotBlank(message = "生成模式不能为空")
    private String generationMode; // BY_DATE, BY_TYPE, BY_SYSTEM

    @ApiModelProperty(value = "生成策略", example = "IMMEDIATE")
    @NotBlank(message = "生成策略不能为空")
    private String generationStrategy; // IMMEDIATE, SCHEDULED

    @ApiModelProperty(value = "业务日期范围开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "业务日期范围结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "凭证日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date voucherDate;

    @ApiModelProperty(value = "事项类型")
    private String transactionType;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "定时执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date scheduledTime;

    @ApiModelProperty(value = "重复执行模式", example = "NONE")
    private String repeatMode = "NONE"; // NONE, DAILY, WEEKLY, MONTHLY

    @ApiModelProperty(value = "生成描述")
    private String description;

    @ApiModelProperty(value = "凭证类型ID")
    private Long voucherTypeId;

    @ApiModelProperty(value = "业务日期范围（前端传递的数组格式）")
    private List<String> businessDateRange;

    /**
     * 获取业务日期范围开始日期
     * 优先使用 startDate，如果为空则从 businessDateRange 解析
     */
    public Date getStartDate() {
        if (startDate != null) {
            return startDate;
        }
        if (businessDateRange != null && businessDateRange.size() >= 1) {
            try {
                return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(businessDateRange.get(0));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取业务日期范围结束日期
     * 优先使用 endDate，如果为空则从 businessDateRange 解析
     */
    public Date getEndDate() {
        if (endDate != null) {
            return endDate;
        }
        if (businessDateRange != null && businessDateRange.size() >= 2) {
            try {
                return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(businessDateRange.get(1));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}