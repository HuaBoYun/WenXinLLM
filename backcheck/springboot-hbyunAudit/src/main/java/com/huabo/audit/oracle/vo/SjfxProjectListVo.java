package com.huabo.audit.oracle.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 审计分析-大屏审计项目列表VO
 * @author AI Assistant
 * @date 2025-01-06
 */
@Data
public class SjfxProjectListVo {

    /**
     * 审计对象组织名称(多个用逗号分隔)
     */
    private String orgname;

    /**
     * 项目名称
     */
    private String pprojectname;

    /**
     * 项目ID
     */
    private BigDecimal projectid;

    /**
     * 计划审计时间(开始日期)
     */
    private String startdate;

    /**
     * 审计类型
     */
    private String audittype;

    /**
     * 责任人姓名(项目经理)
     */
    private String realname;

    /**
     * 主管部门名称
     */
    private String projectorgname;

    /**
     * 项目状态
     */
    private Integer status;

    /**
     * 项目来源
     */
    private String projectsource;

    /**
     * 审计完成时间(结束日期)
     */
    private String enddate;

    /**
     * 项目发现问题数量(问题总数)
     */
    private Integer wtzs;

    /**
     * 问题整改完成数量
     */
    private Integer zgsl;

}
