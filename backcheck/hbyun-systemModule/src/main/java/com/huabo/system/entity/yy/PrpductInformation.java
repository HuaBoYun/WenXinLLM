package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 产品信息
 * @author Zxl
 *
 */
@Data
public class PrpductInformation {
	private Integer informationid;
	/**
	 * 应用类型
	 */
	private String classes;
	
	/**
	 * 产品简称
	 */
	private String filterName;
	/**
	 * 产品图标
	 */
	private String icon;
	/**
	 * 产品分类
	 */
	private String type;
	/**
	 * 简介
	 */
	private String brief;
	/**
	 * 产品名称
	 */
	private String name;
	
	private Integer yycompanyid;
  	private Date yycreatedate;//保存数据时间

	private String uuid;

}
