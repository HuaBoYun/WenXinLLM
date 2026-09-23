package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lsk
 * @date 2023/10/9
 * @description 审计督导报告实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_AUDIT_SUPER_REPORT")
@Schema(name="TblYqnsAuditSuperReport对象", description=" 央企内审-审计实施-审计督导报告")
public class TblYqnsAuditSuperReport implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "ID")
    @Schema(name = "主键ID")
    @Column(name = "ID")
    private Long id;

    /**
     * 报告名称
     */
    @Schema(name = "报告名称")
    @TableField("REPORTNAME")
    private String reportName;

    /**
     * 报告时间
     */
    @Schema(name = "报告时间")
    @TableField("REPORTTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date reportTime;

/*    @TableField(value="REPORTTIME")
    @Schema(name="报告时间")
    private String reportTime;*/

    /**
     * 报告类型
     */
    @Schema(name = "报告类型")
    @TableField("REPORTTYPE")
    private String reportType;

    /**
     * 报告方式
     */
    @Schema(name = "报告方式")
    @TableField("REPORTWAY")
    private String reportWay;

    /**
     * 报告部门
     */
    @Schema(name = "报告部门")
    @TableField("REPORTDEPT")
    private String reportDept;

    /**
     * 报告人
     */
    @Schema(name = "报告人")
    @TableField("REPORTER")
    private String reporter;

    /**
     * 报告内容
     */
    @Schema(name = "报告内容")
    @TableField("REPORTCONTENT")
    private String reportContent;

    /**
     * 附件
     */
    @Schema(name = "附件")
    @TableField("ANNEX")
    private String annex;

    /**
     * 项目id
     */
    @Schema(name = "项目id")
    @TableField("PROJECTID")
    private String projectId;

    @Schema(name = "开始时间")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startTime;

    @Schema(name = "截止时间")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endTime;

    @Schema(name = "审批状态 1-审批中、2-需调整、6-已完成")
    @TableField("STATUS")
    private Integer status;




    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;


}
