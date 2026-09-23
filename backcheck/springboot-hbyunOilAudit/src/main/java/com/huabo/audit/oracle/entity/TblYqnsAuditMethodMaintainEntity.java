package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMethodMaintainEntity
 * @PACKAGE_NAME: com.huabo.audit.oracle.entity
 * @date 2023/10/18 11:03.
 * @version: V1.0
 * @description: 央企内审-基础配置-审计方法维护
 */
@Data
@EqualsAndHashCode(callSuper = false)
@KeySequence(value="AUDIT_OVERSEE_RECORDS_SEQ")
@TableName("TBL_YQNS_AUDIT_METHOD_MAINTAIN")
@Schema(name="TblYqnsAuditMethodMaintain对象", description="央企内审-基础配置-审计方法维护")
public class TblYqnsAuditMethodMaintainEntity extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审计方法维护主键ID
     */
    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "审计方法维护主键ID")
    private Long id;

    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
    
    /**
     * 方法名称
     */
    @Schema(name = "方法名称")
    @TableField("METHODNAME")
    private String methodName ;


    /**
     * 方法内容
     */
    @Schema(name = "方法内容")
    @TableField("METHODCONTENT")
    private String methodContent ;



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
     * 状态（0：模板，1：已绑定） 新增默认：0
     */
    @Schema(name = "状态（0：模板，1：已绑定） 新增默认：0")
    @TableField("STATUS")
    private String STATUS;



    /**
     * 已绑定时,模板审计方法id
     */
    @Schema(name = "已绑定时,模板审计方法id")
    @TableField("METHODID")
    private String methodId;




    /**
     * 状态 0：模板
     */
    public static String STATUS_ZERO="0";

    /**
     * 状态 1：已绑定
     */
    public static String STATUS_ONE="1";





}
