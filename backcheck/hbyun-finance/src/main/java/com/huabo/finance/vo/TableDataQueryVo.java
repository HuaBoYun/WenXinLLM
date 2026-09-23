package com.huabo.finance.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * 表数据查询VO
 *
 * @author system
 * @since 2025-11-22
 */
public class TableDataQueryVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 当前页码
	 */
	private Integer pageNum = 1;

	/**
	 * 每页记录数
	 */
	private Integer pageSize = 20;

	/**
	 * 字段查询条件 (字段名 -> 字段值)
	 */
	private Map<String, String> fieldConditions;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, String> getFieldConditions() {
		return fieldConditions;
	}

	public void setFieldConditions(Map<String, String> fieldConditions) {
		this.fieldConditions = fieldConditions;
	}

}

