package com.huabo.system.util;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrganizeAdministratorCrModel implements Serializable {
	 	
	private static final long serialVersionUID = 1L;
	@Schema(description ="主键")
	private String organizeId;
	@Schema(description ="名称")
	private String fullName;
	@Schema(description ="图标")
	private String icon;
	@Schema(description ="")
	private Boolean isLeaf;
	@Schema(description ="是否有下级菜单")
	private Boolean hasChildren;
	private String parentId;
	
	private Integer thisLayerAdd = 0;
	private Integer thisLayerEdit = 0;
	private Integer thisLayerDelete = 0;
	private Integer thisLayerSelect = 0;
	private Integer subLayerAdd = 0;
	private Integer subLayerEdit = 0;
	private Integer subLayerDelete = 0;
	private Integer subLayerSelect = 0;
	private String organizeIdTree;
	@JsonIgnore
	private String category;
	@Schema(description = "管理组")
	private String managerGroup;
	
	private List<OrganizeAdministratorCrModel> children;
}
