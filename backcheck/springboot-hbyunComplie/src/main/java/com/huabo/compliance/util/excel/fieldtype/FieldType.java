package com.huabo.compliance.util.excel.fieldtype;

/**
 * Excel字段类型转换
 */
public interface FieldType {

	/**
	 * 获取对象值（导入）
	 */
	default Object getValue(String val) {
		return null;
	}

	/**
	 * 获取对象值（导出）
	 */
	default String setValue(Object val) {
		return null;
	}

	/**
	 * 获取对象值格式（导出）
	 */
	default String getDataFormat() {
		return null;
	}

}
