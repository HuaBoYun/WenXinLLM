package com.global.treasurer.util;

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

	/**
	 * 获取页码（兼容方法）
	 */
	public Integer getPageNum() {
		return pageNumber;
	}

	/**
	 * 设置页码（兼容方法）
	 */
	public void setPageNum(Integer pageNum) {
		this.pageNumber = pageNum;
	}

	/**
	 * 获取当前页（兼容方法）
	 */
	public Integer getCurrent() {
		return pageNumber;
	}

	/**
	 * 设置当前页（兼容方法）
	 */
	public void setCurrent(Integer current) {
		this.pageNumber = current;
	}

	/**
	 * 获取每页大小（兼容方法）
	 */
	public Integer getSize() {
		return pageSize;
	}

	/**
	 * 设置每页大小（兼容方法）
	 */
	public void setSize(Integer size) {
		this.pageSize = size;
	}
}
