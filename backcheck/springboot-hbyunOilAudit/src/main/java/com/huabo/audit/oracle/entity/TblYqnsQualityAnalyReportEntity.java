package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsQualityAnalyReportEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/28 14:40
 * @version: V1.0
 * @description: TODO 央企内审-审计实施-质量分析报告
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value="AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_QUALITY_ANALY_REPORT")
@Schema(name="TBL_YQNS_QUALITY_ANALY_REPORT 对象", description="央企内审-审计实施-质量分析报告")
public class TblYqnsQualityAnalyReportEntity extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审查主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "审查主键ID")
    private Long id;

    /**
     * 文号
     */
    @Schema(name = "文号")
    @TableField("DOCUMENTNUMBER")
    private String documentNumber ;


    /**
     * 标题
     */
    @Schema(name = "标题")
    @TableField("TITLE")
    private String title ;



    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField("CREATEUSER")
    private String createUser;


    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @Schema(name = "修改人")
    @TableField("UPDATEUSER")
    private String updateUser;

    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;


    /**
     * 项目id
     */
    @Schema(name = "项目id")
    @TableField("PROJECTID")
    private BigDecimal projectId;

    /**
     * 审批状态
     */
    @Schema(name = "审批状态")
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
