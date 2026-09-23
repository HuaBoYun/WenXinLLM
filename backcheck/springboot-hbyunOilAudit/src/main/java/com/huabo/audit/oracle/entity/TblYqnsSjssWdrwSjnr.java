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
import java.util.Date;
import java.util.List;

/**
 * 央企模块-审计实施-我的任务-审减内容
 * @TableName TBL_YQNS_SJSS_WDRW_SJNR
 */
@TableName(value ="TBL_YQNS_SJSS_WDRW_SJNR")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsSjssWdrwSjnr implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审减内容主键
     */
    @Schema(name = "审减内容主键")
    @TableId(value = "SJNRID",type = IdType.INPUT)
    private Long sjnrid;


    /**
     * 工程ID
     */
    @Schema(name = "工程ID")
    @TableField(value = "PROJECTID")
    private Long projectid;
    
    @Schema(name = "TblYqnsProjectAuditTemplate 主键")
    @TableField("TEMPLATEID")
    private Long templateId;
    
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
     * 工程量计算
     */
    @Schema(name = "工程量计算")
    @TableField(value = "GCLJS")
    private String gcljs;

    /**
     * 定额套用
     */
    @Schema(name = "定额套用")
    @TableField(value = "DETY")
    private String dety;

    /**
     * 物资金额（元）
     */
    @Schema(name = "物资金额（元）")
    @TableField(value = "WZJG")
    private Float wzjg;

    /**
     * 其它审减
     */
    @Schema(name = "其它审减")
    @TableField(value = "QTSJ")
    private String qtsj;

    /**
     * 审减额
     */
    @Schema(name = "审减额")
    @TableField(value = "SJE")
    private Float sje;

    /**
     * 审减人员
     */
    @Schema(name = "审减人员")
    @TableField(value = "SJRY")
    private String sjry;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "BZ")
    private String bz;

    /**
     * 现场实测
     */
    @Schema(name = "现场实测")
    @TableField(value = "XCSC")
    private String xcsc;

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

}