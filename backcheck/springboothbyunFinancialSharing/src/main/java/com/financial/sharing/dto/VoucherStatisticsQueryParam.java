package com.financial.sharing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 凭证统计查询参数
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@ApiModel("凭证统计查询参数")
public class VoucherStatisticsQueryParam {

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "凭证状态(0草稿1已生成2已过账3已取消)")
    private Integer voucherStatus;

    @ApiModelProperty(value = "凭证类型ID")
    private Long voucherTypeId;

    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(value = "会计期间")
    private String accountingPeriod;

    @ApiModelProperty(value = "制单人ID")
    private Long preparer;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "凭证编号")
    private String voucherNo;

    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    @ApiModelProperty(value = "是否包含附件(0否1是)")
    private Integer includeAttachment;

    @ApiModelProperty(value = "统计维度")
    private String dimension;

    @ApiModelProperty(value = "部门ID")
    private Long departmentId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "科目代码")
    private String accountCode;

    @ApiModelProperty(value = "最小金额")
    private Double minAmount;

    @ApiModelProperty(value = "最大金额")
    private Double maxAmount;

    // ========== 兼容旧字段 ==========

    @ApiModelProperty("凭证状态（兼容旧字段）")
    public String getVoucherStatusStr() {
        return voucherStatus != null ? voucherStatus.toString() : null;
    }

    public void setVoucherStatusStr(String voucherStatus) {
        try {
            this.voucherStatus = Integer.parseInt(voucherStatus);
        } catch (NumberFormatException e) {
            this.voucherStatus = null;
        }
    }

    @ApiModelProperty("制单人ID（兼容旧字段）")
    public Long getCreateUserId() {
        return preparer;
    }

    public void setCreateUserId(Long createUserId) {
        this.preparer = createUserId;
    }
}