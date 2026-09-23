package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 欠税公告
 * @author tj
 *
 */
@Data
public class Notice {
	private Integer noticeid;
	private String regType; //注册类型
    private String personIdNumber;//证件号码
    private String legalpersonName; //法人或负责人名称
    private String location; //经营地点
    private String ownTaxAmount; //欠税金额
    private String department; //部门
    private String taxIdNumber; //纳税人识别号
    private String type; //税务类型
    private String taxCategory; //欠税税种
    private String taxpayerType; //纳税人类型
    private String newOwnTaxBalance; //当前新发生欠税余额
    private String ownTaxBalance; //欠税余额
    private String name; //纳税人名称
    private String personIdName; //法人证件名称
    private String publishDate; //发布时间
    private Integer yycompanyid;
 	private Date yycreatedate;//保存数据时间

}
