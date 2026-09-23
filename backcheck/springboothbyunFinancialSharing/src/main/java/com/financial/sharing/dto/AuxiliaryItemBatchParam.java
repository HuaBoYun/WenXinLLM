package com.financial.sharing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 辅助核算项目批量操作参数
 */
@Data
@ApiModel("辅助核算项目批量操作参数")
public class AuxiliaryItemBatchParam {

    @ApiModelProperty("操作类型：ADD-添加，UPDATE-更新，DELETE-删除，ENABLE-启用，DISABLE-禁用")
    private String operationType;

    @ApiModelProperty("辅助核算项ID列表（UPDATE、DELETE、ENABLE、DISABLE时使用）")
    private List<Long> itemIds;

    @ApiModelProperty("辅助核算项数据列表（ADD、UPDATE时使用）")
    private List<AuxiliaryItemData> items;

    @Data
    @ApiModel("辅助核算项数据")
    public static class AuxiliaryItemData {
        @ApiModelProperty("辅助核算项ID（UPDATE时使用）")
        private Long itemId;

        @ApiModelProperty("辅助核算项编码")
        private String auxiliaryCode;

        @ApiModelProperty("辅助核算项名称")
        private String auxiliaryName;

        @ApiModelProperty("辅助核算类型")
        private String auxiliaryType;

        @ApiModelProperty("上级ID")
        private Long parentId;

        @ApiModelProperty("是否启用")
        private Integer isEnabled;

        @ApiModelProperty("排序号")
        private Integer sortOrder;

        @ApiModelProperty("账簿ID")
        private Long bookId;

        @ApiModelProperty("租户ID")
        private Long tenantId;
    }
}