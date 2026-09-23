package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="财务系统API配置请求参数")
public class BdFinversionApiVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Schema(name = "API名称")
	private String handtext;
	
	@Schema(name = "父级主键")
	private String pid;
	
	@Schema(name = "启用状态: 0-禁用 1-启用")
	private Integer enabled;
	
	@Schema(name = "认证方式")
	private String authType;
	
}
