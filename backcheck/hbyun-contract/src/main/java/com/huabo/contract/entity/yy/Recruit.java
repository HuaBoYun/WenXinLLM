package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 招聘
 * @author tj
 *
 */
@Data
public class Recruit {
	private Integer recruitid;
	private String city; //城市
    private String companyName; //招聘公司
    private String createTime; //创建时间
    private String description; //职位描述
    private String district; //所在区
    private String education; //学历
    private String employerNumber; //招聘人数
    private String enddate; //结束日期
    private String experience; //工作经验
    private String id; //对应表id
    private String oriSalary; //薪水
    private String source; //来源
    private String startdate; //开始时间
    private String title; //招聘职称题目
    private String updateTime; //更新时间
    private String urlPath;//www.zhipin.com/job_detail/1415888378.html?sid=aladingb //外网链接
    
    private Integer yycompanyid;
    private Date yycreatedate;//保存数据时间
    
    private String createDate;//创建时间 日期
    private String updateDate; //更新时间 日期

	private String webInfoPath;
	private String companyGid;//企业id
	private String salary; //月薪
	private String startDate;//开始时间

}
