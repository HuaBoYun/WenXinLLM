package com.management.accountant.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页工具类
 * @author wuqian
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableParam {

	@ApiModelProperty("分页当前页数,默认为1")
	private Integer pageNumber = 1;

	@ApiModelProperty("每页记录数,默认为15")
	private Integer pageSize = 15;
}
