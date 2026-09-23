package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 成本归集视图对象
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本归集视图对象")
public class CostCollectionVO {

    @ApiModelProperty("归集ID")
    private Long collectionId;

    @ApiModelProperty("归集ID（字符串格式，用于前端大整数处理）")
    private String collectionIdStr;

    @ApiModelProperty("归集单号")
    private String collectionNo;

    @ApiModelProperty("归集期间")
    private String collectionPeriod;

    @ApiModelProperty("成本中心ID")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("归集类型: 1-直接成本, 2-间接成本, 3-制造费用")
    private Integer collectionType;

    @ApiModelProperty("归集类型名称")
    private String collectionTypeName;

    @ApiModelProperty("归集金额")
    private BigDecimal collectionAmount;

    @ApiModelProperty("归集状态: 1-待归集, 2-归集中, 3-已归集, 4-已审核")
    private Integer collectionStatus;

    @ApiModelProperty("归集状态名称")
    private String collectionStatusName;

    @ApiModelProperty("归集方式: 1-手工归集, 2-自动归集")
    private Integer collectionMethod;

    @ApiModelProperty("归集方式名称")
    private String collectionMethodName;

    @ApiModelProperty("归集日期")
    private String collectionDate;

    @ApiModelProperty("审核人ID")
    private Long auditorId;

    @ApiModelProperty("审核人姓名")
    private String auditorName;

    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty("账簿ID")
    private Long bookId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("归集明细列表")
    private List<CostCollectionDetailVO> detailList;

    /**
     * 成本归集明细视图对象
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("成本归集明细视图对象")
    public static class CostCollectionDetailVO {

        @ApiModelProperty("明细ID")
        private Long detailId;

        @ApiModelProperty("明细ID（字符串格式，用于前端大整数处理）")
        private String detailIdStr;

        @ApiModelProperty("归集ID")
        private Long collectionId;

        @ApiModelProperty("归集ID（字符串格式，用于前端大整数处理）")
        private String collectionIdStr;

        @ApiModelProperty("成本要素编码")
        private String costElementCode;

        @ApiModelProperty("成本要素名称")
        private String costElementName;

        @ApiModelProperty("凭证ID")
        private Long voucherId;

        @ApiModelProperty("凭证号")
        private String voucherNo;

        @ApiModelProperty("会计科目编码")
        private String accountCode;

        @ApiModelProperty("会计科目名称")
        private String accountName;

        @ApiModelProperty("原始金额")
        private BigDecimal originalAmount;

        @ApiModelProperty("分摊率")
        private BigDecimal allocationRate;

        @ApiModelProperty("分摊金额")
        private BigDecimal allocatedAmount;

        @ApiModelProperty("部门名称")
        private String departmentName;

        @ApiModelProperty("项目名称")
        private String projectName;

        @ApiModelProperty("业务类型")
        private String businessType;

        @ApiModelProperty("备注")
        private String remark;
    }
}