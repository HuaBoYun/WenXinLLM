package com.huabo.system.oracle.vo;



import lombok.Data;

import java.math.BigDecimal;

@Data
public class TblFlowAndRiskVo {

    private Integer inflowdb;//0 流程  1行业
    private String company;  //流程所属公司
    private String departincharge;  //责任部门
    private String flownumber;  //编号
    private String flowname;	//流程名称
    private double version;  //版本
    private String departassist;//相关部门
    private BigDecimal fatherflowid;//父id
    private String risknumber;//风险编号
    private String riskname;//风险名称
    private String riskdes;//风险描述
    private String riskprogram;//审计程序
    private String bussinessname;//业务名称
    private String bussinessdes;//业务描述
    private String controlnumber;//风险控制点编号
    private String controlmanager;//控制责任人
    private String toplevelflowcat;//流程分类
    private String controlfrequency;//控制频率
    private String controltype; //控制类型
    private String controlmethod;//控制手段
    private String keycontrol;//是否关键控制
    private String effective;//控制是否有效
    private String controltest;//是否进行控制测试
    private String financialreportidentify;//财务报表认定
    private String controldes;//风险控制点描述
    private String conkzcs;//控制措施
}
