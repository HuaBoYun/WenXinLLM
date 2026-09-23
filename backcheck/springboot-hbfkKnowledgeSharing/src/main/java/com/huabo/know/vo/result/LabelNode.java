package com.huabo.know.vo.result;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Schema(name="标签")
public class LabelNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键ID")
	private String id;
	
	@Schema(name = "标签名称")
	private String label;
	
	@Schema(name = "标签值")
	private String value;

	@Schema(name = "标签类型")
	private String typeValue;

	@Schema(name = "排序")
	private Integer sort;

	@Schema(name = "数量")
	private Integer count;

	@Schema(name = "是否选中")
	private Integer selected;

	@Schema(name = "上级id")
	private String pid;

	@Schema(name = "下级")
	private List<LabelNode> children;
	
}
