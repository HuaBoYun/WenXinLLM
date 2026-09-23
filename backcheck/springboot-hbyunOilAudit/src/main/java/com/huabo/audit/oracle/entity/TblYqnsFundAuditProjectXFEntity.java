package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseProjectEntity;
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
 * TBL_YQNS_ENGIN_AUDIT_PROJ_XF
 * 央企模块-计划编制-财务审计项目 下发
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_FUND_AUDIT_PROJ_XF")
@KeySequence(value = "HIBERNATE_SEQUENCE")
@Schema(name="TblYqnsFundAuditProjectXFEntity对象")
public class TblYqnsFundAuditProjectXFEntity   implements Serializable {
    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }


    /**
     * 分发Id
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "分发Id")
    private Long id;


    /**
     * 财务审计项目主键
     */
    @Schema(name = "财务审计项目主键")
    @TableField(value = "FUNDID")
    private BigDecimal fundId;



    /**
     * userId  用户ID
     */
    @Schema(name = "userId  用户ID")
    @TableField(value = "USERID")
    private String userId;



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

    private static final long serialVersionUID = 1L;
}