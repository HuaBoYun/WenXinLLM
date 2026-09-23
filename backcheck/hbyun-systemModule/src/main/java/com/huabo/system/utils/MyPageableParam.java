package com.huabo.system.utils;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class MyPageableParam {

	@Schema(name="分页当前页数,默认为1")
	private Integer pageNumber = 1;

	@Schema(name="每页记录数,默认为15")
	private Integer pageSize = 15;
}
