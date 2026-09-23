package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 影响因素查询参数
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InfluenceFactorQueryParam", description = "影响因素查询参数")
public class InfluenceFactorQueryParam extends PageableParam {

    @ApiModelProperty(value = "影响因素编码")
    private String factorCode;

    @ApiModelProperty(value = "影响因素名称")
    private String factorName;

    @ApiModelProperty(value = "影响因素类型(1业务类型2科目类型3辅助核算4其他)")
    private Integer factorType;

    @ApiModelProperty(value = "数据类型(1字符串2数值3日期4布尔)")
    private Integer dataType;

    @ApiModelProperty(value = "是否启用(0否1是)")
    private Integer isEnabled;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
}
