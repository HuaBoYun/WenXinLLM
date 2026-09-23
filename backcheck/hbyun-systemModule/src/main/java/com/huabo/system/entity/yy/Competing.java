package com.huabo.system.entity.yy;
import lombok.Data;

import java.util.Date;

/**
 * 竞品信息
 * @author tj
 *
 */
@Data
public class Competing {
	private Integer competingid;
	private String companyId; //对应表id
    private String companyName; //公司名
    private String date;; //时间
    private String graphId; //公司id
    private String hangye; //行业
    private String icon;//logo
    private String iconOssPath; //logo存放位置
    private String isDeleted; //0-未删除 1-删除
    private String jingpinProduct; //竞品名
    private String location; //地区
    private String product; //产品
    private String round; //轮次
    private String setupDate; //投资时间
    private String value;//估值
    private String yewu;//业务范围
    private String dateTime;//时间 
    private Integer yycompanyid;
 	private Date createdate;//保存数据时间

	private String portray;
	private String jingpinBrandId;
	private String jingpinProductId;
	private String portrayStr;
     
}
