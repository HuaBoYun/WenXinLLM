package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证生成预览结果
 *
 * @author system
 * @since 2024-12-08
 */
@Data
@ApiModel("凭证生成预览结果")
public class GenerationPreviewVO {

    @ApiModelProperty(value = "事项ID")
    private Long transactionId;

    @ApiModelProperty(value = "事项编号")
    private String transactionNo;

    @ApiModelProperty(value = "预计凭证号")
    private String voucherNo;

    @ApiModelProperty(value = "凭证类型")
    private String voucherType;

    @ApiModelProperty(value = "凭证日期")
    private Date voucherDate;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "分录条数")
    private Integer entryCount;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "分录详情")
    private List<VoucherEntryPreviewVO> entries;

    @Data
    @ApiModel("凭证分录预览")
    public static class VoucherEntryPreviewVO {
        @ApiModelProperty(value = "科目编码")
        private String subjectCode;

        @ApiModelProperty(value = "科目名称")
        private String subjectName;

        @ApiModelProperty(value = "借方金额")
        private BigDecimal debitAmount;

        @ApiModelProperty(value = "贷方金额")
        private BigDecimal creditAmount;

        @ApiModelProperty(value = "分录摘要")
        private String summary;

        @ApiModelProperty(value = "辅助核算项")
        private String auxiliaryItems;
    }
}