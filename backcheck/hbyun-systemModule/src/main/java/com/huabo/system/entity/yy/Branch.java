package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 分支机构
 * @author tj
 *
 */
@Data
public class Branch {

	private Integer branchid;
	private String id; //公司id
    private String category; //行业code
    private String regCapital; //注册资金
    private String name; //分支名称
    private String base; //省份简称
    private String estiblishTime; //开业时间 毫秒数
    private String regStatus; //经营状态
    private String legalPersonName; //法人
    private String type; //1-公司 2-个人
    private String pencertileScore; //评分
    private String estiblishDate;//开业时间 日期
    private Integer companyid;
    private Date createdate;//保存数据时间
	private String logo; // logo
	private String alias;
	private String personType;

}
