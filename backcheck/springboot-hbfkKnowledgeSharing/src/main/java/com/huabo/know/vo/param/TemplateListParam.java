package com.huabo.know.vo.param;

import com.huabo.know.page.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class TemplateListParam extends BasePageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "标题")
	private String title;

	@Schema(name = "模板类型")
	private String categoryId;

	@Schema(name = "版本标签")
	private String versionTag;
	@Schema(name = "文档类型")
	private Integer templateTypeId;


}
