package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.dto.base.BaseProject;
import com.huabo.audit.oracle.entity.TblYqnsAuditProjectDetailEntity;
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
 * @create: 2023-10-08 09:49
 **/
@Data
public class TblYqnsAuditProjectDto extends BaseProject {

    /**
     * 审计组
     */
    @Schema(name = "审计组", required = true)
    private String auditGroup;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称", required = true)
    private String name;

    /**
     * 现场审计开始时间
     */
    @Schema(name = "现场审计开始时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sceneApproveStaerTime;

    /**
     * 现场审计结束时间
     */
    @Schema(name = "现场审计结束时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sceneApproveEndTime;

    /**
     * 计划现场工作时间
     */
    @Schema(name = "计划现场工作时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planSceneApproveStaerTime;

    /**
     * 计划现场结束时间
     */
    @Schema(name = "计划现场结束时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date planSceneApproveEndTime;

    /**
     * 被审计单位
     */
    @Schema(name = "被审计单位", required = true)
    private String auditUnit;

    /**
     * 被审计单位Id
     */
    @Schema(name = "被审计单位Id", required = true)
    private String auditUnitId;

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

    /**
     * 填报内容
     */
    private List<TblYqnsAuditProjectDetailEntity> auditProjectDetailEntityList;

}
