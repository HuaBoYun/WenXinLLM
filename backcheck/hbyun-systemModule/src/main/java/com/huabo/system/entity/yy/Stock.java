package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 股权出质
 * @author Administrator
 *
 */
@Data
public class Stock {
	private Integer stockid;
	private String equityAmount; //出质股权数额
    private String pledgee; //质权人
    private String regNumber; //登记编号
    private String companyList; //公司列表
    private String putDate; //股权出质设立发布日期 毫秒数
    private String pledgorStr;//出质人拼接字符串
    private String regDate; //股权出质设立登记日期  毫秒数
    private String state; //状态
    private String base; //省份简称
    private String pledgor; //出质人
    private String certifNumberR; //质权人证照/证件号码
    private String certifNumber; //质权人证照/证件号码
    private String pledgeeStr;//质权人拼接字符串
    
    private Integer yycompanyid;
    private Date yycreatedate;//保存数据时间
    
    
    private String regTime ;///股权出质设立登记日期 
    private String putTime;//股权出质设立发布日期
    private String id;
    
    private List<Company> companys;



     
}
