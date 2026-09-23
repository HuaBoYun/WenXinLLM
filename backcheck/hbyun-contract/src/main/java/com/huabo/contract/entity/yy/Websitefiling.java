package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 网站备案
 * @author tjj
 *
 */
@Data
public class Websitefiling {

	private Integer filingid;
	private String webSite;//网站
	private String examineDate;//检查时间
	private String companyType;//公司类型
	private String ym;//域名
	private String webName;//网站名称
	private String companyName;//公司全称
	private String liscense;//许可证
	private Integer yycompanyid;
    private Date yycreatedate;//保存数据时间

}
