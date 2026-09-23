package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="财务版本信息请求参数")
public class BdFinversionVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	   @Schema(name = "文本")
	 private String handtext;
	
	   @Schema(name = "父级主键")
	 private String pid;
	
}
