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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 建设项目投资完成情况汇总表
 * @TableName TBL_YQNS_JSXM_TZWCQKHZ
 */
@TableName(value ="TBL_YQNS_JSXM_TZWCQKHZ")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsJsxmTzwcqkhz extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }
    /**
     * 建设项目投资完成情况主键
     */
    @Schema(name = "建设项目投资完成情况主键")
    @TableId(value = "HZID",type = IdType.INPUT)
    private Long hzid;

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
     * 组织
     */
    @Schema(name = "填报单位")
    @TableField(value = "ORGID")
    private Long orgid;


    /**
     * 审批状态
     */
    @Schema(name = "审批状态")
    @TableField(value = "STATUS")
    private Integer status;
    
    /**
     * 标题
     */
    @Schema(name = "标题")
    @TableField(value = "HZNAME")
    private String hzname;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "HZBZ")
    private String hzbz;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    @Schema(name = "建设项目投资完成情况")
    @TableField(exist = false)
    private List<TblYqnsJsxmTzwcqk> listJsxmTzwcqk;
    
    @Schema(name = "建设项目投资完成情况ids 逗号分隔")
    @TableField(exist = false)
    private String jsxmtzwcqkids;
    
    /**
     * 分类：三类|四类
     */
    @Schema(name = "分类：三类|四类")
    @TableField(value = "FL")
    private String fl;
    
    @Schema(name = "填报单位名称")
    @TableField(exist = false)
    private String orgname;


}