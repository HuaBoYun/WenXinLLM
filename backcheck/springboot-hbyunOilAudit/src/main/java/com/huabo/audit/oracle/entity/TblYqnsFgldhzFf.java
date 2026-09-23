package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author GJ.C
 * @CLASS_NAME: TBL_YQNS_FGLDHZ_Ff
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/12/02 11:03.
 * @version: V1.0
 * @description: 央企内审-计划编制-分管领导汇总表-分发表
 */
@TableName(value = "TBL_YQNS_FGLDHZ_FF")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
@Schema(name="TblYqnsFgldhzFf对象", description="央企内审-计划编制-分管领导汇总表-分发表")
public class TblYqnsFgldhzFf implements Serializable {
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
     * 分管领导汇总主键
     */
    @Schema(name = "分管领导汇总主键")
    @TableField(value = "FGLDHZID")
    private BigDecimal fgldhzId;



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

}