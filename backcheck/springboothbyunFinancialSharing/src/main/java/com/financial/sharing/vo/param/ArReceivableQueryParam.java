package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 应收单据查询参数
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArReceivableQueryParam", description = "应收单据查询参数")
public class ArReceivableQueryParam extends PageableParam {

    @ApiModelProperty(value = "单据编号")
    private String documentNo;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户名称（模糊查询）")
    private String customerName;

    @ApiModelProperty(value = "业务类型(1销售收入 2服务收入 3租赁收入 4其他收入)")
    private Integer businessType;

    @ApiModelProperty(value = "单据状态(0草稿 1待审核 2已审核 3已拒绝)")
    private Integer documentStatus;

    @ApiModelProperty(value = "到期日期开始")
    private LocalDate dueDateStart;

    @ApiModelProperty(value = "到期日期结束")
    private LocalDate dueDateEnd;

    @ApiModelProperty(value = "创建日期开始")
    private LocalDate createDateStart;

    @ApiModelProperty(value = "创建日期结束")
    private LocalDate createDateEnd;

    @ApiModelProperty(value = "是否逾期(0否 1是)")
    private Integer isOverdue;

    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;
}

