package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 央企模块-审计报告-审计工作记录
 * @TableName TBL_YQNS_SJBG_SJGZJL
 */
@TableName(value ="TBL_YQNS_SJBG_SJGZJL")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsSjbgSjgzjl extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 主键
     */
    @Schema(name = "主键")
    @TableId(value = "ID",type = IdType.INPUT)
    private Long id;
    
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
    
    @Schema(name = "实施审理时间")
    @TableField(value = "SSSLSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ssslsj;
    
    @Schema(name = "标题")
    @TableField(value = "TITLE")
    private String title;
    
    @Schema(name = "审计组长")
    @TableField(value = "SJZZ")
    private String sjzz;
    
    @Schema(name = "主审")
    @TableField(value = "ZS")
    private String zs;
    
    @Schema(name = "审理关注的要点")
    @TableField(value = "SLGZYD")
    private String slgzyd;
	
	@Schema(name = "审理发现问题摘要")
    @TableField(value = "SLFAWT")
    private String slfawt;

	@Schema(name = "备注")
    @TableField(value = "MEMO")
    private String memo;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;
    
    @Schema(name = "主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @TableField(value = "PROJECT_ID")
    @Schema(name = "审计项目ID")
    private BigDecimal projectId;

    @TableField(exist = false)
    @Schema(name = "审计项目名称")
    private String projectName;
}