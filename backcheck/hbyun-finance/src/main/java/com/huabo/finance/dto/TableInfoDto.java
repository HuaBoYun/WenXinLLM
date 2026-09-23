package com.huabo.finance.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 表信息DTO
 *
 * @author system
 * @since 2025-11-22
 */
public class TableInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 表备注
	 */
	private String tableComment;

	/**
	 * 查询时间
	 */
	private Date queryTime;

	/**
	 * 分类名称
	 */
	private String category;

	/**
	 * 排序序号
	 */
	private Integer sortOrder;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 字段列表
	 */
	private List<ColumnInfoDto> columns;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public Date getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ColumnInfoDto> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnInfoDto> columns) {
		this.columns = columns;
	}

}

