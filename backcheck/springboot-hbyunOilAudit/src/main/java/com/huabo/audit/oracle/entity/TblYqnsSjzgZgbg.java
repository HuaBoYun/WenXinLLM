package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 央企模块-审计整改-审计整改整改报告及附件
 * @TableName TBL_YQNS_SJZG_ZGBGINFO
 */
@TableName(value ="TBL_YQNS_SJZG_ZGBGINFO")
@Data
public class TblYqnsSjzgZgbg implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 审计整改整改报告主键
     */
    @Schema(name = "审计整改整改报告主键")
    @TableId(value = "ZGBGID",type = IdType.INPUT)
    private BigDecimal zgbgid;
    
    @Schema(name = "问题整改主键")
    @TableField("WTZGID")
    private BigDecimal wtzgid;
    

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;
    
    @Schema(name = "创建人主键")
    @TableField(value = "CJRID")
    private BigDecimal cjrId;
    

    @Schema(name = "所属部门主键")
    @TableField(value = "LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name = "所属公司主键")
    @TableField(value = "LINKORGID")
    private BigDecimal linkOrgId;
    
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
}