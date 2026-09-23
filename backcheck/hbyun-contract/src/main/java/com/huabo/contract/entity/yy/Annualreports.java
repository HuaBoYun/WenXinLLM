package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.List;

/**
 * 企业年报
 * @author Administrator
 *
 */
@Data
public class Annualreports {
	private String baseInfo; //基本信息
	private String companyId; //公司id
	private String changeRecordList;//年报变更
	private String equityChangeInfoList; 
	private String outGuaranteeInfoList; 
	private String outboundInvestmentList; 
	private String shareholderList;
	private String webInfoList;
	private String govReport;
	private String type;
	private String reportSocialSecuritie;
	private BaseInfos baseInfos;
	private List<ChangeRecord> changeRecords;
	private List<EquityChangeInfo> equityChangeInfos;//股东股权变更信息
	private List<OutGuaranteeInfo> outGuaranteeInfos;//对外提供保证担保信息
	private List<OutboundInvestment> outboundInvestments;//对外投资信息
	private List<Shareholder> shareholders;//股东信息
	private List<WebInfo> webInfos;//网站信息
	private String portall;
	private ReportSocialSecurity reportSocialSecurity;//社保信息
	private String employeeNum;
	private String totalAssets;
	private String totalProfit;
	private String totalLiability;
	private String companyName;
	private String postcode;
	private String totalSales;
	private String operatorName;
	private String retainedProfit;
	private String totalTax;
	private String reportYear;
	private String regNumber;
	private String totalEquity;
	private String creditCode;
	private String phoneNumber;
	private String postalAddress;
	private String primeBusProfit;
	private String id;
	private String manageState;
	private String email;
	private String releaseTime;



	
}
