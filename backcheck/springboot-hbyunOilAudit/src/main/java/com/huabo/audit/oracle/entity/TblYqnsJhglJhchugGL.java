package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 计划初稿管理计划 关联表
 *
 * @TableName TBL_YQNS_JHGL_JHCG_MX
 */
@TableName(value = "TBL_YQNS_JHGL_JHCHUG_GL")
@Data
public class TblYqnsJhglJhchugGL implements Serializable {

    /**
     * 计划初稿审计类型g关联主键
     */
    @Schema(name = "计划初稿明细主键")
    @TableId(value = "ID", type = IdType.INPUT)
    private BigDecimal id;


    /**
     * 计划初稿关联id
     */
    @Schema(name = "计划初稿关联id")
    @TableField(value = "JHCHUGID")
    private Long jhchugid;


    /**
     * 关联id
     */
    @Schema(name = "计划类型关联id")
    @TableField(value = "GLID")
    private String glId;

    @Schema(name = "关联单位主键")
    @TableField(value = "RELAORGID")
    private BigDecimal relaOrgId;
    
    @Schema(name = "关联单位名称")
    @TableField(value = "RELAORGNAME")
    private String relaOrgName;
    
    @Schema(name = "项目名称")
    @TableField(value = "PROJECTNAME")
    private String projectName;
    
    @Schema(name = "项目数量")
    @TableField(value = "PROJECTCOUNT")
    private Integer projectCount;
    
    @Schema(name = "项目金额")
    @TableField(value = "PROJECTAMOUNT")
    private BigDecimal projectAmount;
    
    @Schema(name = "备注")
    @TableField(value = "REMARKS")
    private String remarks;
    
    @Schema(name = "计划草稿关联表主键")
    @TableField(value = "RELAID")
    private BigDecimal relaid;
    
    /**
     * 审计类型
     * 1：专项审计
     * 11：生产经营管理专项审计
     * 12：基建与投资专项审计
     * *************************************
     * 2：经济责任审计
     * 21：二级单位及所属成员单位离任经济责任审计
     * 22：二级单位任中经济责任审计
     * 23: 三级单位离任经济责任审计
     * *************************************
     * 3: 工程建设项目审计
     * 31：工程建设项目结算审计
     * 32：工程建设项目竣工决算审计
     */
    @Schema(name = "关联类型")
    @TableField(value = "GLTYPE")
    private String gltype;




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

}