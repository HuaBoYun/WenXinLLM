package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;


/**
 * 企业高管
 * @author Zxl
 *
 *
 */
@Data
public class Executives {
	private Integer executivesid;
	/**
	 * 职位
	 */
	private String position;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 
	 */
	private String graphId;
	/**
	 * 学历
	 */
	private String education;
	/**
	 * 
	 */
	private String cType;
	/**
	 * id
	 */
	private String id;
	/**
	 * 个人简介
	 */
	private String resume;
	/**
	 * 
	 */
	private String managerGroup;
	/**
	 * 
	 */
	private String term;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 年龄
	 */
	private String age;
	/**
	 * 公告日期
	 */
	private String reportDate;
	/**
	 * 薪资
	 */
	private String salary;
	/**
	 * 持股数
	 */
	private String numberOfShares;
	private Integer companyid;
	private Date createdate;//保存数据时间

	private String stockId;
	private String cid;

	private String area;
	private String website;
	private String code;
	private String address;
	private GeneralManager generalManager;

	private String companyName;
	private String employeesNum;
	private String mainBusiness;
	private String mobile;
	private Chairman chairman;
	private String industry;
	private String productName;
	private Secretaries secretaries;
	private String actualController;
	private String controllingShareholder;
	private String engName;
	private String registeredCapital;
	private String postalcode;
	private Legal legal;

	private String fax;
	private String usedName;
	private String finalController;
	private String introduction;

}
