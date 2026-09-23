package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 行政许可
 * @author tjj
 *
 */
@Data
public class Administrative {
	private Integer administrativeid;
	private String fromdate;//日期
	private String licencenumber;
	private String scope;
	private String department;
	private String licencename;
	private Date createdate;//保存数据时间
	
	
	 private Integer companyid;
	 private String licenceContent;
	 private String licenceDepartment;
	 private String areaCode;
	 private String endDate;
	 private String source;
	 private String auditType;
	 private String dataUpdateTime;
	 private String decisionDate;
	 private String legalPersonName;


}
