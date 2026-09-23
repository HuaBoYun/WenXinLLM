package com.huabo.fxgl.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@TableName("TBL_RISK_MONITORINGFILL")
@Schema(name="风险监督指标填报表单")
public class TblRiskMonitoringFill   implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Schema(name = "主键")
    @TableId(value = "ID")
    @Id
    private BigDecimal id;
    
    @Schema(name = "年度")
    @TableField(value = "RISKYEAR")
    private Integer riskyear;

    @Schema(name = "季度名称")
    @TableField(value = "QUARTERNAME")
    private String quartername;
    
    @Schema(name = "备注")
    @TableField(value = "NOTES")
    private String notes;
    
    @Schema(name = "关联风险监测创建表单ID")
    @TableField(value = "MONITORID")
    private BigDecimal monitorId;
 
    @Schema(name = "创建人/填报人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;
    

    @Schema(name = "创建人名称")
    @TableField(exist=false)
    private String createname;

    @Schema(name = "创建时间/填报时间")
    @TableField(value = "CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    @Schema(name = "联系方式")
    @TableField(value="CONTACT")
    private String contact;
    
    @Schema(name = "审批状态")
    @TableField(value = "STATUS")
    private Integer status;
    
    
    //密级及查询条件
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
 
    @Schema(name = "所属公司/填报单位id")
    @TableField("LINKORGID")
    @Column(name = "LINKORGID")
    private BigDecimal linkOrgId;
    
    @Schema(name = "所属公司/填报单位名称")
    @TableField(exist=false)
    private String linkOrgName;
    
    
    @Schema(name = "所属部门/填报单位")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
     private BigDecimal linkDeptId;
    
    @Schema(name = "所属部门名称/填报部门")
    @TableField(exist=false)
     private String linkDeptName;
    
    @TableField(value = "TOREPORTDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(name="上报时间")
    private Date toReportDate;
    
    
    @TableField(value = "REPORTSTAFFID")
    @Schema(name="上报人员id")
    private String  reportStaffid;
    
    @TableField(value = "REPORTSTAFFNAME")
    @Schema(name="上报人员name")
    private String  reportStaffName;
     
    
    @Schema(name = "上报状态 0 未上报  1已上报 2已作废")
    @TableField(value = "REPORTSTATUS")
    private Integer reportstatus;
    //------------------------------------------------------------表单字段
    //战略风险16 
    @TableField(value = "ZLSTRING1")
    @Schema(name="国内外宏观经济形势变化：zlString1")
    private String  zlString1;
    @TableField(value = "ZLSTRING2")
    @Schema(name="国家及行业政策变化：zlString2")
    private String  zlString2;
    @TableField(value = "ZLINTEGER1")
    @Schema(name="被美国列入“实体清单”、受关税政策影响的子企业数量（个）:zlInteger1")
    private BigDecimal  zlInteger1;
    @TableField(value = "ZLINTEGER2")
    @Schema(name="境外重大建设项目逾期数量（个）:zlInteger2")
    private BigDecimal  zlInteger2;
    @TableField(value = "ZLINTEGER3")
    @Schema(name="境外重大法律诉讼案件-数量（个）:zlInteger3")
    private BigDecimal  zlInteger3;
    @TableField(value = "ZLINTEGER4")
    @Schema(name="境外重大合规案件（如被世行禁止参与投标、境外腐败）-数量（个）:zlInteger4")
    private BigDecimal  zlInteger4;
    @TableField(value = "ZLINTEGER5")
    @Schema(name="重大科技项目逾期数量（个）:zlInteger5")
    private BigDecimal  zlInteger5;
    
    @TableField(value = "ZLBIGDECIMAL1")
    @Schema(name="境外中高风险地区境外资产总额-金额（万元）:zlBigdecimal1")
    private BigDecimal  zlBigdecimal1;
    @TableField(value = "ZLBIGDECIMAL2")
    @Schema(name="境外中高风险地区境外资产总额-占资产总额比重:zlBigdecimal2")
    private BigDecimal  zlBigdecimal2;
    @TableField(value = "ZLBIGDECIMAL3")
    @Schema(name="汇率损失金额（万元）:zlBigdecimal3")
    private BigDecimal  zlBigdecimal3;
    @TableField(value = "ZLBIGDECIMAL4")
    @Schema(name="境外重大法律诉讼案件-涉案金额（万元）:zlBigdecimal4")
    private BigDecimal  zlBigdecimal4;
    @TableField(value = "ZLBIGDECIMAL5")
    @Schema(name="境外重大合规案件（如被世行禁止参与投标、境外腐败）-涉案金额（万元）:zlBigdecimal5")
    private BigDecimal  zlBigdecimal5;
    @TableField(value = "ZLBIGDECIMAL6")
    @Schema(name="主要业务板块收入-金额（万元）：zlBigdecimal6")
    private BigDecimal  zlBigdecimal6;
    @TableField(value = "ZLBIGDECIMAL7")
    @Schema(name="主要业务板块收入-占总收入比重：zlBigdecimal7")
    private BigDecimal  zlBigdecimal7;
    @TableField(value = "ZLBIGDECIMAL8")
    @Schema(name="研发投入-金额（万元）：zlBigdecimal8")
    private BigDecimal  zlBigdecimal8;
    @TableField(value = "ZLBIGDECIMAL9")
    @Schema(name="研发投入-占总收入比重：zlBigdecimal9")
    private BigDecimal  zlBigdecimal9;
    
    //财务风险 16 
    @TableField(value = "CWINTEGER1")
    @Schema(name="对外担保业务违约事项-数量（个）:cwInteger1")
    private BigDecimal  cwInteger1;
    @TableField(value = "CWINTEGER2")
    @Schema(name="融资租赁业务违约项目-数量（个）:cwInteger2")
    private BigDecimal  cwInteger2;
    @TableField(value = "CWINTEGER3")
    @Schema(name="债券、股票质押回购违约项目-数量（个）:cwInteger3")
    private BigDecimal  cwInteger3;
    @TableField(value = "CWINTEGER4")
    @Schema(name="信托业务违约项目-数量（个）:cwInteger4")
    private BigDecimal  cwInteger4;
    @TableField(value = "CWINTEGER5")
    @Schema(name="保理业务违约项目-数量（个）:cwInteger5")
    private BigDecimal  cwInteger5;
    
    @TableField(value = "CWBIGDECIMAL1")
    @Schema(name="衍生品盈亏金额（万元）:cwBigdecimal1")
    private BigDecimal  cwBigdecimal1;
    @TableField(value = "CWBIGDECIMAL2")
    @Schema(name="对外担保业务违约事项-金额（万元）:cwBigdecimal2")
    private BigDecimal  cwBigdecimal2;
    @TableField(value = "CWBIGDECIMAL3")
    @Schema(name="融资租赁业务违约项目-金额（万元）:cwBigdecimal3")
    private BigDecimal  cwBigdecimal3;
    @TableField(value = "CWBIGDECIMAL4")
    @Schema(name="债券、股票质押回购违约项目-金额（万元）:cwBigdecimal4")
    private BigDecimal  cwBigdecimal4;
    @TableField(value = "CWBIGDECIMAL5")
    @Schema(name="信托业务违约项目-金额（万元）:cwBigdecimal5")
    private BigDecimal  cwBigdecimal5;
    @TableField(value = "CWBIGDECIMAL6")
    @Schema(name="保理业务违约项目-金额（万元）：cwBigdecimal6")
    private BigDecimal  cwBigdecimal6;
    @TableField(value = "CWBIGDECIMAL7")
    @Schema(name="流动比率：cwBigdecimal7")
    private BigDecimal  cwBigdecimal7;
    @TableField(value = "CWBIGDECIMAL8")
    @Schema(name="资产负债率：cwBigdecimal8")
    private BigDecimal  cwBigdecimal8;
    @TableField(value = "CWBIGDECIMAL9")
    @Schema(name="现金流动负债比率：cwBigdecimal9")
    private BigDecimal  cwBigdecimal9;
    @TableField(value = "CWBIGDECIMAL10")
    @Schema(name="两金（应收账款和存货）总额-金额（万元）：cwBigdecimal10")
    private BigDecimal  cwBigdecimal10;
    @TableField(value = "CWBIGDECIMAL11")
    @Schema(name="两金（应收账款和存货）总额-占流动资产比重：cwBigdecimal11")
    private BigDecimal  cwBigdecimal11;
    @TableField(value = "CWBIGDECIMAL12")
    @Schema(name="现金流风险:经营性现金流：cwBigdecimal12")
    private BigDecimal  cwBigdecimal12;
    @TableField(value = "CWBIGDECIMAL13")
    @Schema(name="现金流风险:一年内到期带息债务是否偿还：cwBigdecimal13")
    private BigDecimal  cwBigdecimal13;
    
    // 市场风险8
    @TableField(value = "SCINTEGER1")
    @Schema(name="主要产品价格下降-数量（个）:scInteger1")
    private BigDecimal  scInteger1;
    @TableField(value = "SCINTEGER2")
    @Schema(name="主要产品市场占有率下降-数量（个）:scInteger2")
    private BigDecimal  scInteger2;
    
    @TableField(value = "SCBIGDECIMAL1")
    @Schema(name="主要产品价格下降-最大降幅:scBigdecimal1")
    private BigDecimal  scBigdecimal1;
    @TableField(value = "SCBIGDECIMAL2")
    @Schema(name="主要产品市场占有率下降-最大降幅:scBigdecimal2")
    private BigDecimal  scBigdecimal2;
    @TableField(value = "SCBIGDECIMAL3")
    @Schema(name="账龄三年及以上的应收账款-金额（万元）:scBigdecimal3")
    private BigDecimal  scBigdecimal3;
    @TableField(value = "SCBIGDECIMAL4")
    @Schema(name="账龄三年及以上的应收账款-占应收账款比重:scBigdecimal4")
    private BigDecimal  scBigdecimal4;
    @TableField(value = "SCBIGDECIMAL5")
    @Schema(name="逾期应收账款-金额（万元）:scBigdecimal5")
    private BigDecimal  scBigdecimal5;
    @TableField(value = "SCBIGDECIMAL6")
    @Schema(name="逾期应收账款-占应收账款比重：scBigdecimal6")
    private BigDecimal  scBigdecimal6;
    //  运营风险10 
    @TableField(value = "YYINTEGER1")
    @Schema(name="亏损子企业-数量（户）:yyInteger1")
    private BigDecimal  yyInteger1;
    @TableField(value = "YYINTEGER2")
    @Schema(name="重大安全生产事故数量（个）:yyInteger2")
    private BigDecimal  yyInteger2;
    @TableField(value = "YYINTEGER3")
    @Schema(name="重大及以上突发环境事件数量（个）:yyInteger3")
    private BigDecimal  yyInteger3;
    @TableField(value = "YYINTEGER4")
    @Schema(name="重大舆情事件数量（个）:yyInteger4")
    private BigDecimal  yyInteger4;
    @TableField(value = "YYINTEGER5")
    @Schema(name="出现重要产品断供的子企业数量（个）:yyInteger5")
    private BigDecimal  yyInteger5;
    @TableField(value = "YYINTEGER6")
    @Schema(name="境内重大建设项目逾期数量（个）:yyInteger6")
    private BigDecimal  yyInteger6;
    @TableField(value = "YYBIGDECIMAL1")
    @Schema(name="亏损子企业-金额（万元）:yyBigdecimal1")
    private BigDecimal  yyBigdecimal1;
    @TableField(value = "YYBIGDECIMAL2")
    @Schema(name="非主业项目投资-金额（万元）:yyBigdecimal2")
    private BigDecimal  yyBigdecimal2;
    @TableField(value = "YYBIGDECIMAL3")
    @Schema(name="非主业项目投资-占总投资额比重:yyBigdecimal3")
    private BigDecimal  yyBigdecimal3;
    @TableField(value = "YYBIGDECIMAL4")
    @Schema(name="年度投资计划完成率（季度实际投资额/全年计划投资额）:yyBigdecimal4")
    private BigDecimal  yyBigdecimal4;

    
    
    //法律风险3  
    @TableField(value = "FLINTEGER1")
    @Schema(name="重大监管处罚数量（个）:flInteger1")
    private BigDecimal  flInteger1;
    @TableField(value = "FLINTEGER2")
    @Schema(name="境内重大法律诉讼案件-数量（个）:flInteger2")
    private BigDecimal  flInteger2;
    @TableField(value = "FLBIGDECIMAL1")
    @Schema(name="境内重大法律诉讼案件-涉案金额（万元）:flBigdecimal1")
    private BigDecimal  flBigdecimal1;
    
    
    //其他风险1
    @TableField(value = "QTSTRING1")
    @Schema(name="其他对企业经营发展造成重大影响的风险:qtString1")
    private String  qtString1;
    //------------------------------以下是情况说明字段
    @TableField(value = "ZLSTRING1DES")
    @Schema(name="国内外宏观经济形势变化情况说明：zlString1Des")
    private String  zlString1Des;
    @TableField(value = "ZLSTRING2DES")
    @Schema(name="国家及行业政策变化情况说明：zlString2Des")
    private String  zlString2Des;
    @TableField(value = "ZLINTEGER1DES")
    @Schema(name="被美国列入“实体清单”、受关税政策影响的子企业数量（个）情况说明:zlInteger1Des")
    private String  zlInteger1Des;
    @TableField(value = "ZLINTEGER2DES")
    @Schema(name="境外重大建设项目逾期数量（个）情况说明:zlInteger2Des")
    private String  zlInteger2Des;
    @TableField(value = "ZLINTEGER3DES")
    @Schema(name="境外重大法律诉讼案件-数量（个）情况说明:zlInteger3Des")
    private String  zlInteger3Des;
    @TableField(value = "ZLINTEGER4DES")
    @Schema(name="境外重大合规案件（如被世行禁止参与投标、境外腐败）-数量（个）情况说明:zlInteger4Des")
    private String  zlInteger4Des;
    @TableField(value = "ZLINTEGER5DES")
    @Schema(name="重大科技项目逾期数量（个）情况说明:zlInteger5Des")
    private String  zlInteger5Des;
    
    @TableField(value = "ZLBIGDECIMAL1DES")
    @Schema(name="境外中高风险地区境外资产总额-金额（万元）情况说明:zlBigdecimal1Des")
    private String  zlBigdecimal1Des;
    @TableField(value = "ZLBIGDECIMAL2DES")
    @Schema(name="境外中高风险地区境外资产总额-占资产总额比重情况说明:zlBigdecimal2Des")
    private String  zlBigdecimal2Des;
    @TableField(value = "ZLBIGDECIMAL3DES")
    @Schema(name="汇率损失金额（万元）情况说明:zlBigdecimal3Des")
    private String  zlBigdecimal3Des;
    @TableField(value = "ZLBIGDECIMAL4DES")
    @Schema(name="境外重大法律诉讼案件-涉案金额（万元）情况说明:zlBigdecimal4Des")
    private String  zlBigdecimal4Des;
    @TableField(value = "ZLBIGDECIMAL5DES")
    @Schema(name="境外重大合规案件（如被世行禁止参与投标、境外腐败）-涉案金额（万元）情况说明:zlBigdecimal5Des")
    private String  zlBigdecimal5Des;
    @TableField(value = "ZLBIGDECIMAL6DES")
    @Schema(name="主要业务板块收入-金额（万元）情况说明：zlBigdecimal6Des")
    private String  zlBigdecimal6Des;
    @TableField(value = "ZLBIGDECIMAL7DES")
    @Schema(name="主要业务板块收入-占总收入比重情况说明：zlBigdecimal7Des")
    private String  zlBigdecimal7Des;
    @TableField(value = "ZLBIGDECIMAL8DES")
    @Schema(name="研发投入-金额（万元）情况说明：zlBigdecimal8Des")
    private String  zlBigdecimal8Des;
    @TableField(value = "ZLBIGDECIMAL9DES")
    @Schema(name="研发投入-占总收入比重情况说明：zlBigdecimal9Des")
    private String  zlBigdecimal9Des;
    //财务风险 16 
    @TableField(value = "CWINTEGER1DES")
    @Schema(name="对外担保业务违约事项-数量（个）情况说明:cwInteger1Des")
    private String  cwInteger1Des;
    @TableField(value = "CWINTEGER2DES")
    @Schema(name="融资租赁业务违约项目-数量（个）情况说明:cwInteger2Des")
    private String  cwInteger2Des;
    @TableField(value = "CWINTEGER3DES")
    @Schema(name="债券、股票质押回购违约项目-数量（个）情况说明:cwInteger3Des")
    private String  cwInteger3Des;
    @TableField(value = "CWINTEGER4DES")
    @Schema(name="信托业务违约项目-数量（个）情况说明:cwInteger4Des")
    private String  cwInteger4Des;
    @TableField(value = "CWINTEGER5DES")
    @Schema(name="保理业务违约项目-数量（个）情况说明:cwInteger5Des")
    private String  cwInteger5Des;
    
    @TableField(value = "CWBIGDECIMAL1DES")
    @Schema(name="衍生品盈亏金额（万元）情况说明:cwBigdecimal1Des")
    private String  cwBigdecimal1Des;
    @TableField(value = "CWBIGDECIMAL2DES")
    @Schema(name="对外担保业务违约事项-金额（万元）情况说明:cwBigdecimal2Des")
    private String  cwBigdecimal2Des;
    @TableField(value = "CWBIGDECIMAL3DES")
    @Schema(name="融资租赁业务违约项目-金额（万元）情况说明:cwBigdecimal3Des")
    private String  cwBigdecimal3Des;
    @TableField(value = "CWBIGDECIMAL4DES")
    @Schema(name="债券、股票质押回购违约项目-金额（万元）情况说明:cwBigdecimal4Des")
    private String  cwBigdecimal4Des;
    @TableField(value = "CWBIGDECIMAL5DES")
    @Schema(name="信托业务违约项目-金额（万元）情况说明:cwBigdecimal5Des")
    private String  cwBigdecimal5Des;
    @TableField(value = "CWBIGDECIMAL6DES")
    @Schema(name="保理业务违约项目-金额（万元）情况说明：cwBigdecimal6Des")
    private String  cwBigdecimal6Des;
    @TableField(value = "CWBIGDECIMAL7DES")
    @Schema(name="流动比率情况说明：cwBigdecimal7Des")
    private String  cwBigdecimal7Des;
    @TableField(value = "CWBIGDECIMAL8DES")
    @Schema(name="资产负债率情况说明：cwBigdecimal8Des")
    private String  cwBigdecimal8Des;
    @TableField(value = "CWBIGDECIMAL9DES")
    @Schema(name="现金流动负债比率情况说明：cwBigdecimal9Des")
    private String  cwBigdecimal9Des;
    @TableField(value = "CWBIGDECIMAL10DES")
    @Schema(name="两金（应收账款和存货）总额-金额（万元）情况说明：cwBigdecimal10Des")
    private String  cwBigdecimal10Des;
    @TableField(value = "CWBIGDECIMAL11DES")
    @Schema(name="两金（应收账款和存货）总额-占流动资产比重情况说明：cwBigdecimal11Des")
    private String  cwBigdecimal11Des;
    @TableField(value = "CWBIGDECIMAL12DES")
    @Schema(name="现金流风险:经营性现金流：cwBigdecimal12Des")
    private String  cwBigdecimal12Des;
    @TableField(value = "CWBIGDECIMAL13DES")
    @Schema(name="现金流风险:一年内到期带息债务是否偿还：cwBigdecimal13Des")
    private String  cwBigdecimal13Des;
    // 市场风险8
    @TableField(value = "SCINTEGER1DES")
    @Schema(name="主要产品价格下降-数量（个）情况说明:scInteger1Des")
    private String  scInteger1Des;
    @TableField(value = "SCINTEGER2DES")
    @Schema(name="主要产品市场占有率下降-数量（个）情况说明:scInteger2Des")
    private String  scInteger2Des;
    
    @TableField(value = "SCBIGDECIMAL1DES")
    @Schema(name="主要产品价格下降-最大降幅情况说明:scBigdecimal1Des")
    private String  scBigdecimal1Des;
    @TableField(value = "SCBIGDECIMAL2DES")
    @Schema(name="主要产品市场占有率下降-最大降幅情况说明:scBigdecimal2Des")
    private String  scBigdecimal2Des;
    @TableField(value = "SCBIGDECIMAL3DES")
    @Schema(name="账龄三年及以上的应收账款-金额（万元）情况说明:scBigdecimal3Des")
    private String  scBigdecimal3Des;
    @TableField(value = "SCBIGDECIMAL4DES")
    @Schema(name="账龄三年及以上的应收账款-占应收账款比重情况说明:scBigdecimal4Des")
    private String  scBigdecimal4Des;
    @TableField(value = "SCBIGDECIMAL5DES")
    @Schema(name="逾期应收账款-金额（万元）情况说明:scBigdecimal5Des")
    private String  scBigdecimal5Des;
    @TableField(value = "SCBIGDECIMAL6DES")
    @Schema(name="逾期应收账款-占应收账款比重情况说明：scBigdecimal6Des")
    private String  scBigdecimal6Des;
    //  运营风险10 
    @TableField(value = "YYINTEGER1DES")
    @Schema(name="亏损子企业-数量（户）情况说明:yyInteger1Des")
    private String  yyInteger1Des;
    @TableField(value = "YYINTEGER2DES")
    @Schema(name="重大安全生产事故数量（个）情况说明:yyInteger2Des")
    private String  yyInteger2Des;
    @TableField(value = "YYINTEGER3DES")
    @Schema(name="重大及以上突发环境事件数量（个）情况说明:yyInteger3Des")
    private String  yyInteger3Des;
    @TableField(value = "YYINTEGER4DES")
    @Schema(name="重大舆情事件数量（个）情况说明:yyInteger4Des")
    private String  yyInteger4Des;
    @TableField(value = "YYINTEGER5DES")
    @Schema(name="出现重要产品断供的子企业数量（个）情况说明:yyInteger5Des")
    private String  yyInteger5Des;
    @TableField(value = "YYINTEGER6DES")
    @Schema(name="境内重大建设项目逾期数量（个）情况说明:yyInteger6Des")
    private String  yyInteger6Des;
    @TableField(value = "YYBIGDECIMAL1DES")
    @Schema(name="亏损子企业-金额（万元）情况说明:yyBigdecimal1Des")
    private String  yyBigdecimal1Des;
    @TableField(value = "YYBIGDECIMAL2DES")
    @Schema(name="非主业项目投资-金额（万元）情况说明:yyBigdecimal2Des")
    private String  yyBigdecimal2Des;
    @TableField(value = "YYBIGDECIMAL3DES")
    @Schema(name="非主业项目投资-占总投资额比重情况说明:yyBigdecimal3Des")
    private String  yyBigdecimal3Des;
    @TableField(value = "YYBIGDECIMAL4DES")
    @Schema(name="年度投资计划完成率（季度实际投资额/全年计划投资额）情况说明:yyBigdecimal4Des")
    private String  yyBigdecimal4Des;
    //法律风险3  
    @TableField(value = "FLINTEGER1DES")
    @Schema(name="重大监管处罚数量（个）情况说明:flInteger1Des")
    private String  flInteger1Des;
    @TableField(value = "FLINTEGER2DES")
    @Schema(name="境内重大法律诉讼案件-数量（个）情况说明:flInteger2Des")
    private String  flInteger2Des;
    @TableField(value = "FLBIGDECIMAL1DES")
    @Schema(name="境内重大法律诉讼案件-涉案金额（万元）情况说明:flBigdecimal1Des")
    private String  flBigdecimal1Des;
    //其他风险1
    @TableField(value = "QTSTRING1DES")
    @Schema(name="其他对企业经营发展造成重大影响的风险情况说明:qtString1Des")
    private String  qtString1Des;
    //--------------------------------------新增预警字段
    //安全环保风险
    @TableField(value = "AQBIGDECIMAL1")
    @Schema(name="辐射安全-一般事故次数：aqBigdecimal1")
    private BigDecimal  aqBigdecimal1;
    @TableField(value = "AQBIGDECIMAL2")
    @Schema(name="辐射安全-具有潜在事故风险的违规事件：aqBigdecimal2")
    private BigDecimal  aqBigdecimal2;
    @TableField(value = "AQBIGDECIMAL3")
    @Schema(name="工业安全-特别重大事故次数：aqBigdecimal3")
    private BigDecimal  aqBigdecimal3;
    @TableField(value = "AQBIGDECIMAL4")
    @Schema(name="工业安全-重大事故次数：aqBigdecimal4")
    private BigDecimal  aqBigdecimal4;
    @TableField(value = "AQBIGDECIMAL5")
    @Schema(name="工业安全-较大事故次数：aqBigdecimal5")
    private BigDecimal  aqBigdecimal5;
    @TableField(value = "AQBIGDECIMAL6")
    @Schema(name="工业安全-一般事故次数：aqBigdecimal6")
    private BigDecimal  aqBigdecimal6;
    @TableField(value = "AQBIGDECIMAL7")
    @Schema(name="职业病-特别重大事故次数：aqBigdecimal7")
    private BigDecimal  aqBigdecimal7;
    @TableField(value = "AQBIGDECIMAL8")
    @Schema(name="职业病-重大事故次数：aqBigdecimal8")
    private BigDecimal  aqBigdecimal8;
    @TableField(value = "AQBIGDECIMAL9")
    @Schema(name="职业病-较大事故次数：aqBigdecimal9")
    private BigDecimal  aqBigdecimal9;
    @TableField(value = "AQBIGDECIMAL10")
    @Schema(name="职业病-一般事故次数：aqBigdecimal10")
    private BigDecimal  aqBigdecimal10;
    @TableField(value = "AQBIGDECIMAL11")
    @Schema(name="突发环境事件次数-特别重大事故次数：aqBigdecimal11")
    private BigDecimal  aqBigdecimal11;
    @TableField(value = "AQBIGDECIMAL12")
    @Schema(name="突发环境事件次数-重大事故次数：aqBigdecimal12")
    private BigDecimal  aqBigdecimal12;
    @TableField(value = "AQBIGDECIMAL13")
    @Schema(name="突发环境事件次数-较大事故次数：aqBigdecimal13")
    private BigDecimal  aqBigdecimal13;
    @TableField(value = "AQBIGDECIMAL14")
    @Schema(name="突发环境事件次数-一般事故次数：aqBigdecimal14")
    private BigDecimal  aqBigdecimal14;
    @TableField(value = "AQBIGDECIMAL15")
    @Schema(name="环境安全-环保行政处罚次数：aqBigdecimal15")
    private BigDecimal  aqBigdecimal15;
   //质量风险
    @TableField(value = "ZLFXBIGDECIMAL1")
    @Schema(name="质量管控-较大及以上质量事故发生次数：zlfxBigdecimal1")
    private BigDecimal  zlfxBigdecimal1;
    @TableField(value = "ZLFXBIGDECIMAL2")
    @Schema(name="质量管控-一般质量事故次数：zlfxBigdecimal2")
    private BigDecimal  zlfxBigdecimal2;
    @TableField(value = "ZLFXBIGDECIMAL3")
    @Schema(name="质量管控-发生质量事件次数：zlfxBigdecimal3")
    private BigDecimal  zlfxBigdecimal3;
    //保密风险
    @TableField(value = "BMBIGDECIMAL1")
    @Schema(name="保密资格认定-一级资格单位未通过数量：bmBigdecimal1")
    private BigDecimal bmBigdecimal1;
    @TableField(value = "BMBIGDECIMAL2")
    @Schema(name="保密资格认定-二级资格单位未通过数量：bmBigdecimal2")
    private BigDecimal bmBigdecimal2;
    @TableField(value = "BMBIGDECIMAL3")
    @Schema(name="保密资格认定-三级资格单位未通过数量：bmBigdecimal3")
    private BigDecimal bmBigdecimal3;
    @TableField(value = "BMBIGDECIMAL4")
    @Schema(name="保密单位保密管理-发生泄密事件次数：bmBigdecimal4")
    private BigDecimal bmBigdecimal4;
    //法律风险
    @TableField(value = "FLBIGDECIMAL2")
    @Schema(name="境内重大法律诉讼案件-公司年度净利润字段:flBigdecimal2")
    private String  flBigdecimal2;
   //战略风险
    @TableField(value = "ZLBIGDECIMAL10")
    @Schema(name="进口物资采购风险-受被美制裁印象，出现重要产品进口受限或断供数量：zlBigdecimal0")
    private BigDecimal  zlBigdecimal10;
    //运营风险
    @TableField(value = "YYBIGDECIMAL5")
    @Schema(name="知识产权-知识产权侵权事件数量（商标等）:yyBigdecimal5")
    private BigDecimal  yyBigdecimal5;
    
    //--------------------------------------新增预警字段情况说明
    //安全环保风险
    @TableField(value = "AQBIGDECIMAL1DES")
    @Schema(name="辐射安全-一般事故次数：aqBigdecimal1Des")
    private String  aqBigdecimal1Des;
    @TableField(value = "AQBIGDECIMAL2DES")
    @Schema(name="辐射安全-具有潜在事故风险的违规事件：aqBigdecimal2Des")
    private String  aqBigdecimal2Des;
    @TableField(value = "AQBIGDECIMAL3DES")
    @Schema(name="工业安全-特别重大事故次数：aqBigdecimal3Des")
    private String  aqBigdecimal3Des;
    @TableField(value = "AQBIGDECIMAL4DES")
    @Schema(name="工业安全-重大事故次数：aqBigdecimal4Des")
    private String  aqBigdecimal4Des;
    @TableField(value = "AQBIGDECIMAL5DES")
    @Schema(name="工业安全-较大事故次数：aqBigdecimal5Des")
    private String  aqBigdecimal5Des;
    @TableField(value = "AQBIGDECIMAL6DES")
    @Schema(name="工业安全-一般事故次数：aqBigdecimal6Des")
    private String  aqBigdecimal6Des;
    @TableField(value = "AQBIGDECIMAL7DES")
    @Schema(name="职业病-特别重大事故次数：aqBigdecimal7Des")
    private String  aqBigdecimal7Des;
    @TableField(value = "AQBIGDECIMAL8DES")
    @Schema(name="职业病-重大事故次数：aqBigdecimal8Des")
    private String  aqBigdecimal8Des;
    @TableField(value = "AQBIGDECIMAL9DES")
    @Schema(name="职业病-较大事故次数：aqBigdecimal9Des")
    private String  aqBigdecimal9Des;
    @TableField(value = "AQBIGDECIMAL10DES")
    @Schema(name="职业病-一般事故次数：aqBigdecimal10Des")
    private String  aqBigdecimal10Des;
    @TableField(value = "AQBIGDECIMAL11DES")
    @Schema(name="突发环境事件次数-特别重大事故次数：aqBigdecimal11Des")
    private String  aqBigdecimal11Des;
    @TableField(value = "AQBIGDECIMAL12DES")
    @Schema(name="突发环境事件次数-重大事故次数：aqBigdecimal12Des")
    private String  aqBigdecimal12Des;
    @TableField(value = "AQBIGDECIMAL13DES")
    @Schema(name="突发环境事件次数-较大事故次数：aqBigdecimal13Des")
    private String  aqBigdecimal13Des;
    @TableField(value = "AQBIGDECIMAL14DES")
    @Schema(name="突发环境事件次数-一般事故次数：aqBigdecimal14Des")
    private String  aqBigdecimal14Des;
    @TableField(value = "AQBIGDECIMAL15DES")
    @Schema(name="环境安全-环保行政处罚次数：aqBigdecimal15Des")
    private String  aqBigdecimal15Des;
   //质量风险
    @TableField(value = "ZLFXBIGDECIMAL1DES")
    @Schema(name="质量管控-较大及以上质量事故发生次数：zlfxBigdecimal1Des")
    private String  zlfxBigdecimal1Des;
    @TableField(value = "ZLFXBIGDECIMAL2DES")
    @Schema(name="质量管控-一般质量事故次数：zlfxBigdecimal2Des")
    private String  zlfxBigdecimal2Des;
    @TableField(value = "ZLFXBIGDECIMAL3DES")
    @Schema(name="质量管控-发生质量事件次数：zlfxBigdecimal3Des")
    private String  zlfxBigdecimal3Des;
    //保密风险
    @TableField(value = "BMBIGDECIMAL1DES")
    @Schema(name="保密资格认定-一级资格单位未通过数量：bmBigdecimal1Des")
    private String bmBigdecimal1Des;
    @TableField(value = "BMBIGDECIMAL2DES")
    @Schema(name="保密资格认定-二级资格单位未通过数量：bmBigdecimal2Des")
    private String bmBigdecimal2Des;
    @TableField(value = "BMBIGDECIMAL3DES")
    @Schema(name="保密资格认定-三级资格单位未通过数量：bmBigdecimal3Des")
    private String bmBigdecimal3Des;
    @TableField(value = "BMBIGDECIMAL4DES")
    @Schema(name="保密单位保密管理-发生泄密事件次数：bmBigdecimal4Des")
    private String bmBigdecimal4Des;
    //法律风险
    @TableField(value = "FLBIGDECIMAL2DES")
    @Schema(name="境内重大法律诉讼案件-公司年度净利润字段:flBigdecimal2Des")
    private String  flBigdecimal2Des;
   //战略风险
    @TableField(value = "ZLBIGDECIMAL10DES")
    @Schema(name="进口物资采购风险-受被美制裁印象，出现重要产品进口受限或断供数量：zlBigdecimal0Des")
    private String  zlBigdecimal10Des;
    //运营风险
    @TableField(value = "YYBIGDECIMAL5DES")
    @Schema(name="知识产权-知识产权侵权事件数量（商标等）:yyBigdecimal5Des")
    private String  yyBigdecimal5Des;
    //----------------------------------------------------------------------------------------
    //战略风险
    @TableField(value = "ZLBIGDECIMAL11")
    @Schema(name="研发投入计划完成情况-研发投入计划完成率：zlBigdecimal11")
    private BigDecimal  zlBigdecimal11;
    //运营风险
    @TableField(value = "YYBIGDECIMAL6")
    @Schema(name="两金增长速度-两金增长百分比:yyBigdecimal6")
    private BigDecimal  yyBigdecimal6;
    //运营风险
    @TableField(value = "YYBIGDECIMAL7")
    @Schema(name="两金增长速度-收入增长百分比:yyBigdecimal7")
    private BigDecimal  yyBigdecimal7;
    //法律风险
    @TableField(value = "FLBIGDECIMAL3")
    @Schema(name="新增法律纠纷案件金额:flBigdecimal3")
    private String  flBigdecimal3;
   //安全环保风险
    @TableField(value = "AQBIGDECIMAL16")
    @Schema(name="发生1级以上核事件次数：aqBigdecimal16")
    private BigDecimal  aqBigdecimal16;
    @TableField(value = "AQBIGDECIMAL17")
    @Schema(name="发生0级核事件次数：aqBigdecimal17")
    private BigDecimal  aqBigdecimal17;
    
    //----------------------------------DES
    //战略风险
    @TableField(value = "ZLBIGDECIMAL11DES")
    @Schema(name="研发投入计划完成情况-研发投入计划完成率：zlBigdecimal11Des")
    private String  zlBigdecimal11Des;
    //运营风险
    @TableField(value = "YYBIGDECIMAL6DES")
    @Schema(name="两金增长速度-两金增长百分比:yyBigdecimal6Des")
    private String  yyBigdecimal6Des;
    //运营风险
    @TableField(value = "YYBIGDECIMAL7DES")
    @Schema(name="两金增长速度-收入增长百分比:yyBigdecimal7Des")
    private String  yyBigdecimal7Des;
    //法律风险
    @TableField(value = "FLBIGDECIMAL3DES")
    @Schema(name="新增法律纠纷案件金额:flBigdecimal3Des")
    private String  flBigdecimal3Des;
   //安全环保风险
    @TableField(value = "AQBIGDECIMAL16DES")
    @Schema(name="发生1级以上核事件次数：aqBigdecimal16Des")
    private String  aqBigdecimal16Des;
    @TableField(value = "AQBIGDECIMAL17DES")
    @Schema(name="发生0级核事件次数：aqBigdecimal17Des")
    private String  aqBigdecimal17Des;
    
    
    
    @TableField(exist=false)
    @Schema(name="备注字段")
    private String  deptNotes;
    
    @TableField(exist=false)
    @Schema(name="关联下发表单")
    private TblFillIssued  tblFillIssued;
    
    @Schema(name = "版本id")
    @TableField(value = "VERSIONID")
    private BigDecimal versionId;
    
    public TblRiskMonitoringFill(){
    	
    }

//下发的时候创建基本数据
	public TblRiskMonitoringFill(Integer riskyear, String quartername, String notes, BigDecimal createstaffid,
			BigDecimal secrectLevelId, BigDecimal linkOrgId, BigDecimal linkDeptId,Date createtime,BigDecimal versionId) {
		super();
		this.riskyear = riskyear;
		this.quartername = quartername;
		this.notes = notes;
		this.createstaffid = createstaffid;
		this.secrectLevelId = secrectLevelId;
		this.linkOrgId = linkOrgId;
		this.linkDeptId = linkDeptId;
		this.createtime = createtime;
		this.versionId = versionId;
	}
}
