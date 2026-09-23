package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;


@Data
public class PeopleInfos {
	private Integer peopleinfosid;
	private String licenseNum;//证照/证件号码
	private String peopleName;//抵押权人名称
	private String liceseType;//抵押权人证照/证件类型
	private Integer companyid;
	private Date createdate;//保存数据时间
	private Chattel chattel;

}
