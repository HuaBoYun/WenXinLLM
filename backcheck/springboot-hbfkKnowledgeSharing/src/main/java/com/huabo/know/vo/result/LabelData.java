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
@Schema(name="工具栏标签数据")
public class LabelData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "标签名称")
	private String label;
	
	@Schema(name = "标签值")
	private String value;

	@Schema(name = "数据")
	private List<LabelNode> data;
	
}
