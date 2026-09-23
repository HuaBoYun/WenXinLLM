package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMethodMaintenanEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/18 12:01.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的底稿-下方审计查证事实
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value = "AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_AUDIT_MY_MANU_VERIFY")
@Schema(name="TblYqnsAuditMyManuVerifyEntity对象", description="央企内审-审计实施-我的底稿-下方审计查证事实")
public class TblYqnsAuditMyManuVerifyEntity implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 工程审计类型主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "审计查证事实ID")
    private Long id;



    /**
     * 审计查证事实描述是否详尽、充分
     */
    @Schema(name = "审计查证事实")
    @TableField("VERIFICATIONDESCRIPTION")
    private String verificationDescription ;


    /**
     * 审计结论及依据
     */
    @Schema(name = "审计结论及依据")
    @TableField("AUDITCONCLUSION")
    private String auditConclusion ;

    /**
     * 审计处理意见及建议
     */
    @Schema(name = "审计处理意见及建议")
    @TableField("HANDLINGOPINIONS")
    private String handlingOpinions ;
 

    /**
     * 我的底稿ID
     */
    @Schema(name = "我的底稿ID")
    @TableField("MYMANUSCRIPTID")
    private String myManuscriptId;



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
     * 备注
     */
    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    /**
     * 项目id
     */
    @Schema(name = "项目id")
    @TableField("PROJECTID")
    private BigDecimal projectId;

    /**
     * 预留字段
     */
    @Schema(name = "预留字段")
    @TableField("RESERVED")
    private String reserved;
    
    
    @Schema(name = "状态（1当前记录  0历史记录）")
    @TableField(value = "STATUS")
    private Integer status;
    
}
