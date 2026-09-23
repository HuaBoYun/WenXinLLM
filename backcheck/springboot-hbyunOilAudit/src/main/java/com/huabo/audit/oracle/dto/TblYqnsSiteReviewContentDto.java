package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.TblYqnsSiteReviewAttEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-09 14:38
 **/
@Data
public class TblYqnsSiteReviewContentDto {

    /**
     * 现场审查主要内容主键
     */
    @Schema(name = "现场审查主要内容主键ID")
    private Long id;

    /**
     * 删除标识：1是；0否
     */
    @Schema(name = "删除标识：1是；0否")
    private BigDecimal deleted;

    /**
     * 结算项目编号
     */
    @Schema(name = "结算项目编号")
    private String settleProjectNum;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    private String projectName;

    /**
     * 结算金额,单位分
     */
    @Schema(name = "结算金额,单位分")
    private BigDecimal settleAmount;

    /**
     * 建设单位ID
     */
    @Schema(name = "建设单位ID")
    private Long buildOrgId;

    /**
     * 施工单位ID
     */
    @Schema(name = "施工单位ID")
    private Long constructionOrgId;

    /**
     * 核实主要内容
     */
    @Schema(name = "核实主要内容")
    private String reviewContent;

    /**
     * 审计人员ID
     */
    @Schema(name = "审计人员ID")
    private Long reviewStaffId;

    /**
     * 建设单位项目经理ID
     */
    @Schema(name = "建设单位项目经理ID")
    private BigDecimal buildUnitManageId;

    /**
     * 现场审查时间
     */
    @Schema(name = "现场审查时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sceneReviewTime;

    /**
     * 复审人员ID
     */
    @Schema(name = "复审人员ID")
    private BigDecimal recheckStaffId;

    /**
     * 建设单位名称
     */
    @Schema(name = "建设单位名称")
    private String buildOrgName;

    /**
     * 施工单位名称
     */
    @Schema(name = "施工单位名称")
    private String constructionOrgName;

    /**
     * 审计人员名称
     */
    @Schema(name = "审计人员名称")
    private String reviewStaffName;

    /**
     * 建设单位项目经理名称
     */
    @Schema(name = "建设单位项目经理名称")
    private String buildUnitManageName;

    /**
     * 复审人员名称
     */
    @Schema(name = "复审人员名称")
    private String recheckStaffName;

    /**
     * 审批状态
     */
    @Schema(name = "审批状态")
    private Integer status;

    /**
     * 项目id
     */
    @Schema(name = "项目id")
    private BigDecimal projectId;
    
    @Schema(name = "TblYqnsProjectAuditTemplate 主键")
    private Long templateId;

    /**
     * 附件集合
     */
    private List<TblYqnsSiteReviewAttEntity> siteReviewAttEntityList;

}
