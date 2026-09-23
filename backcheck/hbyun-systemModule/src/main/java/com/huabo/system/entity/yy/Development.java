package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 投资事件
 * @author tj
 *
 */
@Data
public class Development {
	
	private Integer developmentid;
	private String company_id; //对应表id
    private String iconhttp; //logo
    private String location; //地区
    private String yewu; //业务范围
    private String hangye1; //行业
    private String iconOssPath; //logo存放位置
    private String tzdate; //投资时间
    private String product; //产品名
    private String id; //产品id
    private String graph_id; //公司id
    private String company; //公司名
    private String money; //金额
    private String lunci; //轮次
    private String rongzi_map; //投资公司
    private String organization_name; //投资公司
    private String tzTime;//投资时间 日期
    private Integer companyid;
 	private Date createdate;//保存数据时间

	private String icon;
     
}
