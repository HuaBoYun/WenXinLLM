package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 央企模块-计划编制-审计立项建议通知
 * @TableName TBL_YQNS_SJBG_JJZRSJJGBG
 */
@TableName(value ="TBL_YQNS_PROPOSAL_NOTICE")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
@Schema(name="TblYqnsProposalNoticeEntity对象", description="央企模块-计划编制-审计立项建议通知")
public class TblYqnsProposalNoticeEntity implements Serializable {



    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审计立项建议通知主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "审计立项建议通知主键ID")
    private Long id;

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


    /**
     * 内容
     */
    @Schema(name = "内容")
    @TableField(value = "CONTENT")
    private String content;



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