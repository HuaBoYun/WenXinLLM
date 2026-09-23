package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 法院公告
 * @author tj
 *
 */
@Data
public class Judicialrisk {
	private Integer judicialriskid;
    private String id; //对应表id
    private String announce_id; //公告id
    private String bltnno; //公告号
    private String bltnstate; //公告状态号
    private String bltntype;//公告类型
    private String bltntypename; //公告类型名称
    private String caseno; //案件号
    private String content; //案件内r容
    private String courtflag; //无用
    private String courtcode; //法院名
    private String customno;
    private String dealgrade;  //处理等级
    private String dealgradename; //处理等级名称
    private String judge; //法官
    private String judgephone; //法官电话
    private String mobilephone; //手机号
    private String party1; //原告
    private String party2; //当事人
    private String companyList; //公司列表 (数组)
    private String party1Str; //无用
    private String party2Str; //无用
    private String province; //省份
    private String publishdate; //刊登日期
    private String publishpage; //刊登版面
    private String reason; //原因
    private String showtxtdate; //无用
    private String tmpsaversn; //无用
    private String uuid;
    private String explainState;
    private String explainReason;
    private String explainMessage;
    private String explainId;
    private String businessId;
    
    private Integer yycompanyid;
   	private Date yycreatedate;//保存数据时间
    
    private List<Company> companys;

    
}
