package com.huabo.finance.dto;

import java.io.Serializable;

/**
 * 表存在性检查DTO
 * 
 * @author system
 * @since 2025-11-22
 */
public class TableExistsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 是否存在
	 */
	private Boolean exists;
	
	/**
	 * 表备注
	 */
	private String tableComment;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Boolean getExists() {
		return exists;
	}

	public void setExists(Boolean exists) {
		this.exists = exists;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}
	
}

