package com.financial.sharing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 字段映射批量操作参数
 */
@Data
@ApiModel("字段映射批量操作参数")
public class FieldMappingBatchParam implements Serializable {

    @ApiModelProperty(value = "租户ID", hidden = true)
    @JsonIgnore
    private Long tenantId;

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @ApiModelProperty(value = "更新用户ID", hidden = true)
    @JsonIgnore
    private Long updateUser;

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    @ApiModelProperty(value = "操作类型：ENABLE-启用，DISABLE-禁用，DELETE-删除", example = "ENABLE", required = true)
    @NotBlank(message = "操作类型不能为空")
    private String operationType;

    @ApiModelProperty(value = "映射ID列表", required = true)
    @NotEmpty(message = "映射ID列表不能为空")
    private List<Long> mappingIds;
}