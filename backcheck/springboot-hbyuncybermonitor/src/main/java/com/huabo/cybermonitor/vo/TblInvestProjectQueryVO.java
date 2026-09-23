package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投资项目台账查询VO - 投资穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TblInvestProjectQueryVO extends BaseVo {

    /** 项目名称（模糊查询） */
    private String projectName;

    /** 项目编号 */
    private String projectCode;

    /** 所属企业ID */
    private String companyId;

    /** 所属企业名称（模糊查询） */
    private String companyName;

    /** 投资类型 */
    private String investType;

    /** 投资类别 */
    private String investCategory;

    /** 审批状态 */
    private String approvalStatus;

    /** 项目状态 */
    private String projectStatus;

    /** 决策层级 */
    private String decisionLevel;

    /** 关键词搜索 */
    private String keyword;
}

