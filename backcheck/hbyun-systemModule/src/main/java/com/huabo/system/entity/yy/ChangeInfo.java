package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;


@Data
public class ChangeInfo {
	private Integer changeInfoid;
	private String changeDate;//变更日期
	private String changeContent;//变更内容
	private Integer companyid;
	private Date createdate;//保存数据时间
	
	private Chattel chattel;
}
