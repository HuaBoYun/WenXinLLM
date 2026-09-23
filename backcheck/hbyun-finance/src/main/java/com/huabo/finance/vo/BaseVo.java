package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseVo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Schema(name = "起始页数")
	private Integer pageNumber = 1;
	 
	@Schema(name = "每页数量")
	private Integer pageSize = 20;
	
}
