package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 基本信息
 * @author tj
 *
 */
@Data
public class BaseInfos {
	private Integer baseinfosid;
	private String reportYear; //年份
    private String companyName; //公司名
    private String creditCode; //统一社会信用代码
    private String regNumber; //注册码
    private String phoneNumber; //联系方式
    private String postcode; //邮编
    private String postalAddress; //邮编地址
    private String email; //邮箱
    private String manageState; //经营状态
    private String employeeNum; //从业人数
    private String operatorName; //经营者名称
    private String totalAssets; //资产总额
    private String totalEquity; //所有者权益合计
    private String totalSales; //销售总额(营业总收入)
    private String totalProfit; //利润总额
    private String primeBusProfit; //主营业务收入
    private String retainedProfit; //净利润
    private String totalTax; //纳税总额
    private String totalLiability; //负债总额
    
    
    
    
    //动产抵押
    private String overviewAmount; //概况数额
    private String scope; //担保范围
    private String status; //状态
    private String remark; //备注
    private String regDate; //登记日期
    private String overviewType; //概况种类
    private String type; //被担保债权种类
    private String cancelReason; //注销原因
    private String overviewScope; //概况担保的范围
    private String id; //表id
    private String amount; //被担保债权数额
    private String overviewRemark; //概况备注
    private String overviewTerm; //概况债务人履行债务的期限
    private String regDepartment; //登记机关
    private String regNum; //登记编号
    private String term; //债务人履行债务的期限
    private String base; //省份
    private String cancelDate; //注销日期
    private String publishDate; //公示日期
    
    private Date createdate;//保存数据时间
    private Date dccreatedate;//动产抵押保存数据时间
    private Integer companyid;

    
}
