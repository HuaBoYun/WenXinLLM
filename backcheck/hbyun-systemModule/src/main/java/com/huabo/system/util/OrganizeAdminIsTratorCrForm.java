package com.huabo.system.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrganizeAdminIsTratorCrForm implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
     * 用户主键
     **/
    @Schema(description = "用户主键")
    @NotBlank(message = "管理员不能为空")
    private BigDecimal userId;

    /**
     * 分级管理员模型集合
     */
    @Schema(description = "分级管理员模型集合")
    private List<OrganizeAdministratorCrModel> orgAdminModel;


    @Schema(description = "菜单集合")
    private List<String> moduleIds;
    @Schema(description = "应用集合")
    private List<String> systemIds;
    @Schema(description = "管理组")
    private String managerGroup;
}
