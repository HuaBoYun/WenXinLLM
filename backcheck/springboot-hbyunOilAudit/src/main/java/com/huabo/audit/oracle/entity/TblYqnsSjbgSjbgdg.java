package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 央企模块-审计报告-审计报告定稿
 * @TableName TBL_YQNS_SJBG_SJBGDG
 */
@TableName(value ="TBL_YQNS_SJBG_SJBGDG")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsSjbgSjbgdg extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审计报告定稿主键
     */
    @Schema(name = "审计报告定稿主键")
    @TableId(value = "SJBGDGID",type = IdType.INPUT)
    private Long sjbgdgid;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;

    /**
     * 更新人
     */
    @Schema(name = "更新人")
    @TableField(value = "GXR")
    private String gxr;

    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    @TableField(value = "GXSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gxsj;

    /**
     * 标题
     */
    @Schema(name = "标题")
    @TableField(value = "TITLE")
    private String title;

    /**
     * 文号
     */
    @Schema(name = "文号")
    @TableField(value = "DOCUMENT")
    private String document;

    @Schema(name="开始时间",hidden=true)
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;

    @Schema(name="结束时间",hidden=true)
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @Schema(name = "主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;

    @TableField(value = "STATUS")
    @Schema(name = "状态:")
    private Integer status;

    @TableField(value = "PROJECT_ID")
    @Schema(name = "审计项目ID")
    private BigDecimal projectId;

    @TableField(exist = false)
    @Schema(name = "审计项目名称")
    private String projectName;
    
    @TableField(exist = false)
    @Schema(name = "主审名称")
    private String zsname;
    
    @TableField(exist = false)
    @Schema(name = "查询区分")
    private String type;
}