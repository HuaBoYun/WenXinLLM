package com.huabo.know.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Schema(name="模板类型node")
public class TemplateCategoryNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键ID")
	private String id;
	
	@Schema(name = "类型名称")
	private String name;
	
	@Schema(name = "标签类型值")
	private String code;

	@Schema(name = "描述")
	private String description;

	@Schema(name = "上级id")
	private String pid;

	@Schema(name = "排序")
	private Integer sort;

	@Schema(name = "templates")
	private String templates;

	@Schema(name = "所有父级名称")
	private String parentNames;

	@Schema(name = "level")
	private Integer categoryLevel;


	@Schema(name = "下级")
	private List<TemplateCategoryNode> children;
	
}
