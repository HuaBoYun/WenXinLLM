package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会计科目查询参数
 */
@Data
@ApiModel("会计科目查询参数")
public class AccountSubjectQueryParam {

    @ApiModelProperty("账套ID")
    private Long bookId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("科目编码")
    private String subjectCode;

    @ApiModelProperty("科目名称")
    private String subjectName;

    @ApiModelProperty("科目类型")
    private String subjectType;

    @ApiModelProperty("父科目ID")
    private Long parentId;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("页大小")
    private Integer pageSize = 10;
}