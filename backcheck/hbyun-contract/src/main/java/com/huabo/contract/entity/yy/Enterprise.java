package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 企业信息
 * @author tj
 *
 */
@Data
public class Enterprise {
	private Integer enterpriseid;
	private String companyName; //公司名
    private String createTime; //创建时间
    private String detailUrl; //无用
    private String hangye; //行业
    private String id; //id
    private String isDeleted; //0-未删除 1-删除
    private String logo;//logo
    private String logoOssPath; //logo存放位置
    private String product;//产品名
    private String productId; //产品id
    private String setupDate; //上线时间
    private String yewu; //业务范围
    private String brandId;
    private String sourceWeb;
    private String updateTime;
    private String graphId;//公司id
    private String companyId;//对应表id
    
    private Integer yycompanyid;
   	private Date yycreatedate;//保存数据时间
      
      
	private String createDate;//创建时间 日期
	private String setupTime;//上线时间 日期

	private String round;
	private String location;
	private String base;

}
