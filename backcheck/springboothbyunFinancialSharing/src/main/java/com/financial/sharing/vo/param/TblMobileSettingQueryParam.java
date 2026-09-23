package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 移动设置查询参数
 */
@Data
@ApiModel("移动设置查询参数")
public class TblMobileSettingQueryParam {

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页大小")
    private Integer pageSize = 10;

    @ApiModelProperty("设置编码")
    private String settingCode;

    @ApiModelProperty("设置名称")
    private String settingName;

    @ApiModelProperty("设置类型")
    private String settingType;

    @ApiModelProperty("是否启用")
    private Integer isEnabled;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("组织ID")
    private String orgId;
}

