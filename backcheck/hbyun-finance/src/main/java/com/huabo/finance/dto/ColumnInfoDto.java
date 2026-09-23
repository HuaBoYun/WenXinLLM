package com.huabo.finance.dto;

import java.io.Serializable;

/**
 * 字段信息DTO
 * 
 * @author system
 * @since 2025-11-22
 */
public class ColumnInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 字段名
	 */
	private String columnName;
	
	/**
	 * 字段备注
	 */
	private String columnComment;
	
	/**
	 * 数据类型
	 */
	private String dataType;
	
	/**
	 * 是否允许空值 (Y/N)
	 */
	private String nullable;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	
}

