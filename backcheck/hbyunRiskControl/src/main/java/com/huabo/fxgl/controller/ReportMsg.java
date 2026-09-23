package com.huabo.fxgl.controller;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 报告消息实体类
 * <p>封装风险报告的消息数据</p>
 *
 * @author hbyun
 */
@Data
public class ReportMsg {

    public static String type;
    private String id;
    private String selectProjectid;
    private String reporttype;
    private String reportmode;
    private String reportdepartment;
    private String reportfile;
    private String memo;
    private String reporter;
    private String reporttempid;
    private BigDecimal orgid;
    private BigDecimal fhstaffid;
    private BigDecimal zqyjstaffid;
    private String reporterid;
    private String attids;

}
