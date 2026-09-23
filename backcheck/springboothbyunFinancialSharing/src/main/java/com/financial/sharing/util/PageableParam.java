package com.financial.sharing.util;

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
public class PageableParam<T> {

	@ApiModelProperty("分页当前页数,默认为1")
	private Integer pageNumber = 1;

	@ApiModelProperty("每页记录数,默认为15")
	private Integer pageSize = 15;

	@ApiModelProperty("查询参数")
	private T param;

	/**
	 * 获取页码 - 兼容方法
	 * @return 页码
	 */
	public Integer getPageNum() {
		return this.pageNumber;
	}

	/**
	 * 设置页码 - 兼容方法
	 * @param pageNum 页码
	 */
	public void setPageNum(Integer pageNum) {
		this.pageNumber = pageNum;
	}

	/**
	 * 获取页码 - 兼容方法 (从0开始)
	 * @return 页码
	 */
	public Integer getPage() {
		return this.pageNumber - 1;
	}

	/**
	 * 获取每页大小 - 兼容方法
	 * @return 每页大小
	 */
	public Integer getSize() {
		return this.pageSize;
	}

	/**
	 * 设置页码
	 * @param pageNumber 页码
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * 设置每页大小
	 * @param pageSize 每页大小
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 设置查询参数
	 * @param param 查询参数
	 */
	public void setParam(T param) {
		this.param = param;
	}

	/**
	 * 获取页码
	 * @return 页码
	 */
	public Integer getPageNumber() {
		return this.pageNumber;
	}

	/**
	 * 获取每页大小
	 * @return 每页大小
	 */
	public Integer getPageSize() {
		return this.pageSize;
	}

	/**
	 * 获取查询参数
	 * @return 查询参数
	 */
	public T getParam() {
		return this.param;
	}

	// 兼容方法：获取页码
	public Integer getPageNo() {
		return this.pageNumber;
	}

	// 兼容方法：设置页码
	public void setPageNo(Integer pageNo) {
		this.pageNumber = pageNo;
	}

	// 兼容方法：获取条件
	public T getCondition() {
		return this.param;
	}

	// 兼容方法：设置条件
	public void setCondition(T condition) {
		this.param = condition;
	}
}
